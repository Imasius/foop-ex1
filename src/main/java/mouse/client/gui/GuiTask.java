package mouse.client.gui;

import mouse.client.cfg.ClientConfiguration;
import mouse.client.network.ServerConnection;

import java.awt.*;

/**
 * Created by Florian on 2014-04-12.
 */
public class GuiTask implements Runnable {
    private final ServerConnection connection;

    public GuiTask(ServerConnection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        MouseJFrame mjfGameFrame = new MouseJFrame(connection);

        if (ClientConfiguration.INSTANCE.isFullscreen()) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] gs = ge.getScreenDevices();
            gs[0].setFullScreenWindow(mjfGameFrame);
        }
        else {
            mjfGameFrame.setVisible(true);
        }
    }
}