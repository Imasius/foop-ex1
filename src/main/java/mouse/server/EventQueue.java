package mouse.server;

import mouse.server.network.MessageWrapper;

import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import mouse.server.event.GameLogicEventListener;

/**
 * User: Simon Date: 04.06.2014
 */
public class EventQueue {

    private Timer timer;
    private final EventQueueTask eventQueueTask;
    private final long interval;

    public EventQueue(long interval) {
        this.interval = interval;
        this.eventQueueTask = new EventQueueTask();
    }

    public void start() {
        timer = new Timer();

        timer.scheduleAtFixedRate(eventQueueTask, interval, interval);
    }

    public void stop() {
        timer.cancel();
    }

    public BlockingQueue<MessageWrapper> getQueue() {
        return eventQueueTask.getQueue();
    }

    public void registerGameLogicEventListener(GameLogicEventListener logicEventListener) {
        this.eventQueueTask.registerGameLogicEventListener(logicEventListener);
    }
}
