package mouse.server.network;

import mouse.server.EventQueue;
import mouse.server.ServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * The server for our mouse game. Use this class inside the client to create a server
 * without running a separate process. See StandaloneServer for a example on usage.
 *
 * User: Simon
 * Date: 21.03.14
 */
public class MouseServer {

    private static final Logger log = LoggerFactory.getLogger(MouseServer.class);

    private ServerSocket serverSocket;
    private final Broadcaster broadcaster;
    private final ServerConfiguration serverConfiguration;
    private final EventQueue eventQueue;

    public MouseServer() {
        serverConfiguration = ServerConfiguration.load();
        broadcaster = new Broadcaster(serverConfiguration);
        eventQueue = new EventQueue();
    }

    /**
     * @return True if the server startup was successful.
     */
    public boolean start() {
        log.info("Starting mouse game server.");

        try {
            serverSocket = new ServerSocket(serverConfiguration.getServerPort());
            log.debug("Server is listening on port {}", serverConfiguration.getServerPort());
        } catch (IOException e) {
            log.error("Unable to create server socket.", e);
            return false;
        }

        ClientListener clientListener = new ClientListener(serverSocket, eventQueue, serverConfiguration.getPlayerCount());
        clientListener.start();

        if (serverConfiguration.isBroadcastEnabled())
            broadcaster.start();

        eventQueue.start();

        return true;
    }

    public void stop() {
        log.info("Shutting down server.");
        try {
            serverSocket.close();
        } catch (IOException ex) {
            log.warn("Exception while closing the ServerSocket.", ex);
        }

        if (serverConfiguration.isBroadcastEnabled())
            broadcaster.stop();

        eventQueue.interrupt();
    }
}
