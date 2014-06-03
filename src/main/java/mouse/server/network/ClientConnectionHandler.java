package mouse.server.network;

import mouse.server.EventQueue;
import mouse.shared.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handles communication with a specific client.
 *
 * User: Simon
 * Date: 26.03.14
 */
public class ClientConnectionHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(ClientConnectionHandler.class);

    private static AtomicInteger idCounter = new AtomicInteger(0);

    private final Socket socket;
    private final EventQueue queue;
    private final int clientId;

    public ClientConnectionHandler(Socket socket, EventQueue queue) {
        this.socket = socket;
        this.queue = queue;
        this.clientId = idCounter.getAndIncrement();
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            log.error("Unable to obtain InputStream from socket.", e);
        }

        while (true) {
            Message message = Message.fromStream(inputStream);
            try {
                queue.getQueue().put(new MessageWrapper(message, this));
            } catch (InterruptedException e) {
                log.warn("Interrupted during put.", e);
            }
        }
    }

    public int getClientId() {
        return clientId;
    }
}
