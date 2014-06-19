package mouse.server;

import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import mouse.server.simulation.event.GameLogicEventListener;
import mouse.shared.messages.clientToServer.ClientToServerMessage;
import mouse.shared.messages.clientToServer.ClientToServerMessageListener;

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

    public BlockingQueue<ClientToServerMessage> getQueue() {
        return eventQueueTask.getQueue();
    }

    /**
     * Pass listener Registration to the timer task
     */
    public void addGameLogicEventListener(GameLogicEventListener l) {
        eventQueueTask.addGameLogicEventListener(l);
    }

    /**
     * Pass Listener Registration to the timer task
     */
    public void addClientToServerMessageListener(ClientToServerMessageListener l) {
        eventQueueTask.addClientToServerMessageListener(l);
    }
}
