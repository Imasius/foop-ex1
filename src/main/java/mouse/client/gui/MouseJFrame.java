package mouse.client.gui;

import java.awt.*;
import javax.swing.*;

import mouse.client.network.ServerConnection;
import mouse.shared.Door;
import mouse.shared.messages.clientToServer.RequestDoorStateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date: 06.04.2014
 *
 * @author Kevin Streicher,Florian Schober
 */
public class MouseJFrame extends JFrame implements MouseCanvasListener {

    private static final Logger log = LoggerFactory.getLogger(MouseJFrame.class);
    private final MouseCanvas mcCanvas;
    private final ServerConnection connection;

    public MouseJFrame(ServerConnection connection) {
        log.debug("Initialized MouseJFrame");

        this.mcCanvas = new MouseCanvas();
        this.mcCanvas.addMouseCanvasListener(this);

        if (connection != null) {
            this.connection = connection;
            this.connection.addServerToClientMessageListener(mcCanvas);
            new Thread(this.connection).start();
        } else {
            this.connection = null;
            log.debug("No Server specified - started serverless dummy-client");
        }

        //TODO switch to miglayout and set JFrame size and position
        setLayout(new BorderLayout());
        add(mcCanvas, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
    }

    //TODO: remove canvas init hack!
    @Override
    public void setVisible(boolean b) {
        super.setVisible(b); //To change body of generated methods, choose Tools | Templates.
        mcCanvas.init();
    }

    private void maximize() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth());
        int ySize = ((int) tk.getScreenSize().getHeight());
        setSize(xSize, ySize);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    public void handleDoorClicked(Door door) {
        connection.sendMessage(new RequestDoorStateMessage(door));
        log.debug("Sent request to toggle door:" + door.getPosition().x + "," + door.getPosition().y);
    }
}
