package mouse.server.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import mouse.shared.messages.Message;
import mouse.shared.messages.clientToServer.ClientToServerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles communication with a specific client.
 *
 * User: Simon Date: 26.03.14
 */
public class ClientConnectionHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(ClientConnectionHandler.class);

    private static AtomicInteger idCounter = new AtomicInteger(0);

    private final Socket socket;
    private final BlockingQueue<ClientToServerMessage> queue;
    private final int clientId;

    public ClientConnectionHandler(Socket socket, BlockingQueue<ClientToServerMessage> queue) {
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
                queue.put(new MessageWrapper(message, this));
            } catch (InterruptedException e) {
                log.warn("Interrupted during put.", e);
            }
        }
    }

    public int getClientId() {
        return clientId;
    }

    public void sendMessage(Message m) {
        try {
            m.writeToStream(socket.getOutputStream());
        } catch (IOException ex) {
            log.warn("Unable to send message {} to client {}", m.toString(), toString());
        }
    }

    @Override
    public String toString() {
        return "ClientConnectionHandler: " + clientId + " Remotadress:" + socket.getRemoteSocketAddress().toString();
    }
}
