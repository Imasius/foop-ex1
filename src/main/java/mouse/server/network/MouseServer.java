package mouse.server.network;

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

    public static final short DEFAULT_PORT = 30330;

    private final short port;
    private ServerSocket serverSocket;

    public MouseServer(short port) {
        this.port = port;
    }

    public void start() {
        log.info("Starting mouse game server. Specified port is {}.", port);

        try {
            serverSocket = new ServerSocket(port);
            log.debug("Server is listening on port {}", port);
        } catch (IOException e) {
            log.error("Unable to create server socket.", e);
            return;
        }

        ClientListener clientListener = new ClientListener(serverSocket);
        clientListener.start();
    }

    public void stop() {
        log.info("Shutting down server.");
        try {
            serverSocket.close();
        } catch (IOException ex) {
            log.warn("Exception while closing the ServerSocket.", ex);
        }
    }
}
