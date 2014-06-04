package mouse.server.network;

import mouse.server.simulation.LevelAdapter;
import mouse.server.simulation.Mouse;
import mouse.server.simulation.ServerLevel;
import mouse.shared.messages.GameStartMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class will use accept client connections and create appropriate objects
 * for each client.
 * <p/>
 * User: Simon
 * Date: 26.03.14
 */
public class ClientListener extends Thread {

    private static final Logger log = LoggerFactory.getLogger(ClientListener.class);

    private final ServerSocket serverSocket;
    private final BlockingQueue<MessageWrapper> queue;
    private final int clientCount;

    public ClientListener(ServerSocket serverSocket, BlockingQueue<MessageWrapper> queue, int clientCount) {
        this.serverSocket = serverSocket;
        this.queue = queue;
        this.clientCount = clientCount;
    }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Socket> clientList = new ArrayList<Socket>();

        try {
            while (true) {
                Socket client = serverSocket.accept();
                clientList.add(client);

                log.debug("Client connected from IP address {}", client.getInetAddress());

                ClientConnectionHandler clientConnectionHandler = new ClientConnectionHandler(client, queue);
                executorService.execute(clientConnectionHandler);

                if (clientList.size() == clientCount) {
                    log.info("Desired player count reached ({}). Starting game.", clientCount);

                    ServerLevel level = new ServerLevel(1);
                    List<Mouse> mice = new ArrayList<Mouse>();
                    Iterator<Point> iterator = level.getStartPositions().iterator();
                    for (int i = 0; i < clientList.size(); i++) {
                        Point start = iterator.next();
                        Mouse mouse = new Mouse(start, new LevelAdapter(level));
                        level.addMouse(mouse);
                        mice.add(mouse);
                    }

                    GameStartMessage message = ServerLevel.toGameStartMessage(level);
                    for (Socket socket : clientList) {
                        message.writeToStream(socket.getOutputStream());
                    }
                }
            }
        } catch (IOException e) {
            executorService.shutdown();

            log.debug("Shutting down all client connections. Currently {} clients.", clientList.size());
            for (Socket client : clientList) {
                try { client.close(); } catch (IOException ex) { /* ignored */ }
            }
        }
    }
}