package mouse.server.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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

    public ClientListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
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

                ClientConnectionHandler clientConnectionHandler = new ClientConnectionHandler(client);
                executorService.execute(clientConnectionHandler);
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
