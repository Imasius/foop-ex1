package mouse.server.network;

import java.net.Socket;

/**
 * Handles communication with a specific client.
 *
 * User: Simon
 * Date: 26.03.14
 */
public class ClientConnectionHandler extends Thread {

    private final Socket socket;

    public ClientConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // Communicate with each client in here
    }
}
