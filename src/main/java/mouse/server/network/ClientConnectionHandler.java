package mouse.server.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import mouse.server.network.event.NotificationEventListener;
import mouse.server.network.event.PlayerLeftEvent;
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
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private final BlockingQueue<ClientToServerMessage> queue;
    private final int clientId;

    private ArrayList<NotificationEventListener> listener = new ArrayList<NotificationEventListener>();

    private boolean shutdown = false;

    public ClientConnectionHandler(Socket socket, BlockingQueue<ClientToServerMessage> queue) {
        this.socket = socket;
        this.queue = queue;
        this.clientId = idCounter.getAndIncrement();
        try {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            log.error("Unable to create ObjectOutputStream for:" + socket.getRemoteSocketAddress().toString());
        }
        try {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            log.error("Unable to create ObjectOutputStream for:" + socket.getRemoteSocketAddress().toString());
        }
    }

    public void setShutdown(boolean shutdown) {
        this.shutdown = shutdown;
    }

    public void registerNotificationEventListener(NotificationEventListener l) {
        listener.add(l);
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
            ClientToServerMessage message = null;
            try {
                message = ClientToServerMessage.fromStream(objectInputStream);
            } catch (IOException ex) {
                shutdown = true;
                PlayerLeftEvent event = new PlayerLeftEvent(this);
                for (NotificationEventListener l : listener) {
                    l.handlePlayerLeftEvent(event);
                }
                return;
            }
            try {
                queue.put(message);
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
            m.writeToStream(objectOutputStream);
        } catch (IOException ex) {
            PlayerLeftEvent event = new PlayerLeftEvent(this);
            for (NotificationEventListener l : listener) {
                l.handlePlayerLeftEvent(event);
            }
        }
    }

    @Override
    public String toString() {
        return "ClientConnectionHandler: " + clientId + " Remotadress:" + socket.getRemoteSocketAddress().toString();
    }
}
