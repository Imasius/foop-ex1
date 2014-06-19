package mouse.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import mouse.server.simulation.event.GameLogicEventListener;
import mouse.shared.messages.clientToServer.ClientToServerMessage;
import mouse.shared.messages.clientToServer.ClientToServerMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Simon on 03.06.2014.
 */
public class EventQueueTask extends TimerTask {

    private static final Logger log = LoggerFactory.getLogger(EventQueueTask.class);
    private final BlockingQueue<ClientToServerMessage> queue = new LinkedBlockingQueue<ClientToServerMessage>();
    private final ArrayList<GameLogicEventListener> gameLogicEventListeners = new ArrayList<GameLogicEventListener>();
    private final ArrayList<ClientToServerMessageListener> clientToServerMessageListeners = new ArrayList<ClientToServerMessageListener>();

    public void run() {
        while (true) {
            ClientToServerMessage message = queue.poll();
            if (message == null) {
                break;
            }
            message.alertListeners(clientToServerMessageListeners);
        }
        fireTickEvent();
    }

    public BlockingQueue<ClientToServerMessage> getQueue() {
        return queue;
    }

    /**
     * The GameLogicEventListener hierachy encapsulates all events not handled
     * by the server and provides an interface between the network layer and the
     * logic layer
     */
    public void addGameLogicEventListener(GameLogicEventListener l) {
        gameLogicEventListeners.add(l);
    }

    /**
     * The ClientToServerMessageListeners handle the events sent from clients
     *
     * @param l
     */
    public void addClientToServerMessageListener(ClientToServerMessageListener l) {
        clientToServerMessageListeners.add(l);
    }

    private void fireTickEvent() {
        Iterator<GameLogicEventListener> it = gameLogicEventListeners.iterator();
        while (it.hasNext()) {
            it.next().handleTick();
        }
    }

}
