package mouse.client;

import javax.swing.SwingUtilities;

import mouse.client.cfg.ClientConfiguration;
import mouse.client.gui.GuiTask;
import mouse.client.network.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Simon Date: 21.03.14
 */
public class MouseClient {

    private static final Logger log = LoggerFactory.getLogger(MouseClient.class);

    public static void main(String[] args) {
        log.debug("Starting up client.");

        ClientConfiguration.INSTANCE.parseCommandLine(args);

        BroadcastReceiver receiver = new BroadcastReceiver(ClientConfiguration.INSTANCE);
        receiver.addListener(new BroadcastReceiverListener() {
            @Override
            public void onServerFound(ServerInfo info) {
                SwingUtilities.invokeLater(new GuiTask(new ServerConnection(info)));
            }
        });
        new Thread(receiver).start();
    }
}
