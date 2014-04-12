package mouse.client.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

import mouse.client.network.ServerConnection;
import mouse.client.network.ServerConnectionObserver;
import mouse.shared.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date: 06.04.2014
 *
 * @author Kevin Streicher
 */
public class MouseJFrame extends JFrame implements KeyListener, ActionListener, MouseListener {

    private static final Logger log = LoggerFactory.getLogger(MouseJFrame.class);
    private MouseCanvas mcCanvas;
    private ServerConnection connection;

    public MouseJFrame(ServerConnection connection) {
        log.debug("Initialized MouseJFrame");

        this.connection = connection;
        this.connection.addObserver(mcCanvas.getLevel());
        this.connection.addObserver(new ServerConnectionObserver() {
            @Override public void onMouseMoved(int mouseIdx, Point newPosition) {}              // handled by level
            @Override public void onDoorStateChanged(Point doorPosition, boolean isClosed) {}   // handled by level
            @Override public void onGameStart(Level level) {}                                   // handled by level

            @Override
            public void onGameOver() {
                JOptionPane.showMessageDialog(null, "Game over");
            }
        });
        new Thread(this.connection).start();

        //TODO switch to miglayout and set JFrame size and position
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        mcCanvas = new MouseCanvas();
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

    public void keyTyped(KeyEvent e) {
        //TODO: Implement KeyHandling typed
    }

    public void keyPressed(KeyEvent e) {
        //TODO: Implement KeyHandling pressed
    }

    public void keyReleased(KeyEvent e) {
        //TODO: Implement KeyHandling released
    }

    public void actionPerformed(ActionEvent e) {
        //TODO: Implement action performed
    }

    public void mouseClicked(MouseEvent e) {
        //TODO: Implement Mouse clicked        
    }

    public void mousePressed(MouseEvent e) {
        //TODO: Implement Mouse pressed
    }

    public void mouseReleased(MouseEvent e) {
        //TODO: Implement Mouse released
    }

    public void mouseEntered(MouseEvent e) {
        //TODO: Implement Mouse entered
    }

    public void mouseExited(MouseEvent e) {
        //TODO: Implement Mouse exit
    }
}