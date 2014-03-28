package mouse.server.network;

import mouse.server.ServerConfiguration;
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

    private final ServerConfiguration serverConfiguration;

    private Timer timer;
    private BroadcastTask broadcastTask;

    public Broadcaster(ServerConfiguration serverConfiguration) {
        this.serverConfiguration = serverConfiguration;
    }

    public void start() {
        timer = new Timer();
        broadcastTask = new BroadcastTask(serverConfiguration);

        log.debug("Starting the task for IP address broadcasting. IP sent is: {}", broadcastTask.getSentAddress());
        timer.schedule(broadcastTask, 1000, serverConfiguration.getMulticastInterval());
    }

    public void stop() {
        log.debug("Shutting down the multicast task.");
        timer.cancel();
        broadcastTask.closeSocket();
    }
}
