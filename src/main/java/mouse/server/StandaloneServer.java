package mouse.server;

import mouse.server.network.MouseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This class is intended to run without a client. Mainly for dev and debug purposes.
 *
 * User: Simon
 * Date: 26.03.14
 */
public class StandaloneServer {

    private static final Logger log = LoggerFactory.getLogger(StandaloneServer.class);

    public static void main(String[] args) {
        log.info("Starting mouse server in standalone version");

        MouseServer server = new MouseServer();
        server.start();

        log.info("Press any key to shutdown the standalone server");

        try {
            System.in.read();
            server.stop();
        } catch (IOException ex) {
            log.warn("Exception while listening on System.in.", ex);
        }
    }
}
