package mouse.server.network;

import mouse.server.simulation.event.GameLogicEventListener;
import mouse.server.simulation.event.PlayerJoinedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import mouse.shared.messages.clientToServer.ClientToServerMessage;

/**
 * This class will use accept client connections and create appropriate objects
 * for each client.
 *
 * The ClientListener is also responsible for closing the sockets, therefore it
 * stores a list of sockets as clientList.
 * <p/>
 * User: Simon Date: 26.03.14 User: Kevin Date: 17.06.14
 */
public class ClientListener extends Thread {

    private static final Logger log = LoggerFactory.getLogger(ClientListener.class);

    private final ServerSocket serverSocket;
    private final BlockingQueue<ClientToServerMessage> queue;
    private final ArrayList<GameLogicEventListener> listeners = new ArrayList<GameLogicEventListener>();
    private final List<Socket> clientList = new ArrayList<Socket>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public ClientListener(ServerSocket serverSocket, BlockingQueue<ClientToServerMessage> queue) {
        this.serverSocket = serverSocket;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket client = serverSocket.accept();
                clientList.add(client);
                log.debug("Client connected from IP address {}", client.getInetAddress());
                firePlayerJoinedEvent(client);
            }
        } catch (IOException e) {
            executorService.shutdown();

            log.debug("Shutting down all client connections. Currently {} clients.", clientList.size());
            for (Socket client : clientList) {
                try {
                    client.close();
                } catch (IOException ex) { /* ignored */ }
            }
        }
    }

    public void registerGameLogicEventListener(GameLogicEventListener l) {
        listeners.add(l);
    }

    private void firePlayerJoinedEvent(Socket client) {
        //Create a ClientConnectionHandler who is responsible for reading network messages from the client
        ClientConnectionHandler clientConnectionHandler = new ClientConnectionHandler(client, queue);
        executorService.execute(clientConnectionHandler);

        Iterator<GameLogicEventListener> it = listeners.iterator();
        while (it.hasNext()) {
            it.next().handlePlayerJoinedEvent(new PlayerJoinedEvent(clientConnectionHandler));
        }

    }
}
