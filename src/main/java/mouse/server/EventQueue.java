package mouse.server;

import mouse.server.network.MessageWrapper;
import mouse.shared.MouseState;
import mouse.shared.Tile;
import mouse.shared.messages.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Simon on 03.06.2014.
 */
public class EventQueue extends Thread implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(EventQueue.class);

    private final BlockingQueue<MessageWrapper> queue = new LinkedBlockingQueue<MessageWrapper>();

    public void run() {
        try {
            while (true) {
                MessageWrapper wrappedMessage = queue.take();
                log.info("Received message of type {} by client with id {}",
                        wrappedMessage.getMessage().getClass().getName(), wrappedMessage.getClient().getClientId());
                wrappedMessage.getMessage().alertListeners(Arrays.asList(this));
            }
        } catch (InterruptedException e) {
            log.info("EventQueue got interrupted. Shutting down.");
        }
    }

    public BlockingQueue getQueue() {
        return queue;
    }

    @Override
    public void onMouseMoved(int mouseIdx, MouseState newState) {

    }

    @Override
    public void onDoorStateChanged(Point doorPosition, boolean isClosed) {

    }

    @Override
    public void onGameOver() {

    }

    @Override
    public void onTryChangeDoorState(Point doorPosition, boolean tryClose) {

    }

    @Override
    public void onGameStart(Tile[][] tiles, Point baitPosition, Collection<Point> startPositions, Collection<MouseState> mice) {

    }
}
