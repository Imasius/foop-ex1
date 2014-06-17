package mouse.server;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import mouse.server.simulation.event.CloseDoorEvent;
import mouse.server.simulation.event.GameLogicEventListener;
import mouse.server.simulation.event.OpenDoorEvent;
import mouse.shared.Door;
import mouse.shared.messages.clientToServer.ClientToServerMessage;
import mouse.shared.messages.clientToServer.ClientToServerMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Simon on 03.06.2014.
 */
public class EventQueueTask extends TimerTask implements ClientToServerMessageListener {

    private static final Logger log = LoggerFactory.getLogger(EventQueueTask.class);
    private final BlockingQueue<ClientToServerMessage> queue = new LinkedBlockingQueue<ClientToServerMessage>();
    private final ArrayList<GameLogicEventListener> listeners = new ArrayList<GameLogicEventListener>();

    public void run() {
        while (true) {
            ClientToServerMessage message = queue.poll();
            if (message == null) {
                break;
            }
            message.alertListeners(Arrays.asList(this));
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
    public void registerGameLogicEventListener(GameLogicEventListener l) {
        listeners.add(l);
    }

    private void fireOpenDoorEvent(Point doorPosition) {
        Iterator<GameLogicEventListener> it = listeners.iterator();
        while (it.hasNext()) {
            it.next().handleOpenDoorEvent(new OpenDoorEvent(doorPosition));
        }
    }

    private void fireCloseDoorEvent(Point doorPosition) {
        Iterator<GameLogicEventListener> it = listeners.iterator();
        while (it.hasNext()) {
            it.next().handleCloseDoorEvent(new CloseDoorEvent(doorPosition));
        }
    }

    private void fireTickEvent() {
        Iterator<GameLogicEventListener> it = listeners.iterator();
        while (it.hasNext()) {
            it.next().handleTick();
        }
    }

    public void handleOpenDoor(Door door) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleCloseDoor(Door door) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
