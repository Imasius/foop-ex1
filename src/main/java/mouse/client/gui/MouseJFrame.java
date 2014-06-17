package mouse.client.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.*;

import mouse.client.network.ServerConnection;
import mouse.client.network.ServerConnectionListener;
import mouse.server.simulation.Mouse;
import mouse.shared.Door;
import mouse.shared.MouseState;
import mouse.shared.Tile;
import mouse.shared.messages.serverToClient.ServerToClientMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date: 06.04.2014
 *
 * @author Kevin Streicher
 */
public class MouseJFrame extends JFrame {

    class LocalMouseCanvasListener implements MouseCanvasListener {

        @Override
        public void onDoorClicked(Point door, boolean isClosed) {
            connection.requestDoorState(door, !isClosed);
            log.debug("Clicked on a door @" + door.x + "," + door.y);
        }
    }

    class LocalServerConnectionListener implements ServerToClientMessageListener {

        public void handleUpdateMice(ArrayList<Mouse> mice) {
            mcCanvas.repaint();
        }

        public void handleUpdateDoors(ArrayList<Door> doors) {
            mcCanvas.repaint();
        }

        public void handleGameStart(Tile[][] tiles, Point baitPosition, Collection<Point> startPositions, Collection<MouseState> mice) {
            mcCanvas.repaint();
            log.debug("Game started");
        }

        public void handleGameOver() {
            log.debug("Game over");
            mcCanvas.repaint();
            JOptionPane.showMessageDialog(null, "Game over");
        }
    }

    private static final Logger log = LoggerFactory.getLogger(MouseJFrame.class);
    private final MouseCanvas mcCanvas;
    private final ServerConnection connection;

    public MouseJFrame(ServerConnection connection) {
        log.debug("Initialized MouseJFrame");

        this.mcCanvas = new MouseCanvas();
        this.mcCanvas.addListener(new LocalMouseCanvasListener());

        if (connection != null) {
            this.connection = connection;
            this.connection.addListener(mcCanvas.getLevel());
            this.connection.addListener(new LocalServerConnectionListener());
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
}
