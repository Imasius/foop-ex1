package mouse.server;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import mouse.server.event.CloseDoorEvent;
import mouse.server.event.GameLogicEventListener;
import mouse.server.event.OpenDoorEvent;
import mouse.server.network.MessageWrapper;
import mouse.shared.MouseState;
import mouse.shared.Tile;
import mouse.shared.messages.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Simon on 03.06.2014.
 */
public class EventQueueTask extends TimerTask implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(EventQueueTask.class);
    private final BlockingQueue<MessageWrapper> queue = new LinkedBlockingQueue<MessageWrapper>();
    private final ArrayList<GameLogicEventListener> listeners = new ArrayList<GameLogicEventListener>();

    public void run() {
        while (true) {
            MessageWrapper wrappedMessage = queue.poll();
            if (wrappedMessage == null) {
                break;
            }
            log.info("Received message of type {} by client with id {}",
                    wrappedMessage.getMessage().getClass().getName(), wrappedMessage.getClient().getClientId());
            wrappedMessage.getMessage().alertListeners(Arrays.asList(this));
        }
        fireTickEvent();
    }

    public BlockingQueue<MessageWrapper> getQueue() {
        return queue;
    }

    @Override
    public void onMouseMoved(int mouseIdx, MouseState newState) {
        /* ignored by server */
    }

    @Override
    public void onDoorStateChanged(Point doorPosition, boolean isClosed) {
        /* ignored by server */
    }

    @Override
    public void onGameOver() {
        /* ignored by server */
    }

    @Override
    public void onTryChangeDoorState(Point doorPosition, boolean tryClose) {
        log.info("Attempted door change {} to {}.", doorPosition, tryClose);
        if (tryClose) {
            fireCloseDoorEvent(doorPosition);
        } else {
            fireOpenDoorEvent(doorPosition);
        }
    }

    @Override
    public void onGameStart(Tile[][] tiles, Point baitPosition, Collection<Point> startPositions, Collection<MouseState> mice) {
        /* ignored by server */
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

}
