package mouse.server.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;

/**
 * Will send out (periodically) multicast messages containing this mouse servers ip address as text.
 *
 * User: Simon
 * Date: 26.03.14
 */
public class Broadcaster {

    private static final Logger log = LoggerFactory.getLogger(Broadcaster.class);

    public static final String MULTICAST_ADDRESS = "224.0.100.100";
    public static final short MULTICAST_PORT = 30331;

    private static final int TASK_INTERVAL = 2000;

    private final Timer timer;
    private final BroadcastTask broadcastTask;

    public Broadcaster() {
        timer = new Timer();
        broadcastTask = new BroadcastTask(MULTICAST_ADDRESS, MULTICAST_PORT);
    }

    public void start() {
        log.debug("Starting the task for IP address broadcasting. IP sent is: {}", broadcastTask.getSentAddress());
        timer.schedule(broadcastTask, 1000, TASK_INTERVAL);
    }

    public void stop() {
        log.debug("Shutting down the multicast task.");
        timer.cancel();
        broadcastTask.closeSocket();
    }
}
