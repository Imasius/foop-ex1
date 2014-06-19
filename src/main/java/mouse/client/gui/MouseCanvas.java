package mouse.client.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.*;
import java.util.List;
import javax.swing.JOptionPane;

import mouse.client.data.ClientLevel;
import mouse.shared.Door;
import mouse.shared.LevelStructure;
import mouse.shared.Mouse;
import mouse.shared.Tile;
import mouse.shared.messages.serverToClient.ServerToClientMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MouseCanvas extends Canvas implements MouseListener, ServerToClientMessageListener {

    private static final Logger log = LoggerFactory.getLogger(MouseCanvas.class);
    private BufferStrategy strategy;
    private LevelDrawer levelDrawer;
    private ClientLevel level;
    private final List<MouseCanvasListener> listeners;
    private HashMap<Integer, Integer> score = new HashMap<Integer, Integer>();

    //TODO set IgnoreRepaint to true
    public MouseCanvas() {
        this.level = null;
        this.levelDrawer = new LevelDrawer(this.level);
        this.listeners = new ArrayList<MouseCanvasListener>();
        setIgnoreRepaint(false);
        this.addMouseListener(this);
    }

    public ClientLevel getLevel() {
        return this.level;
    }

    public void addMouseCanvasListener(MouseCanvasListener listener) {
        this.listeners.add(listener);
    }

    public void init() {
        setVisible(true);
        createBufferStrategy(2);
        this.strategy = getBufferStrategy();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        renderFrame();
    }

    private void renderFrame() {
        if (strategy == null) {
            return;
        }
        Graphics2D g2d = null;
        try {
            g2d = (Graphics2D) strategy.getDrawGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            DrawArgs args = new DrawArgs(getWidth(),
                    getHeight(),
                    g2d);

            levelDrawer.draw(args);
            for (int i = 0; i < score.size(); i++) {
                g2d.setColor(Color.white);
                g2d.drawString("Player " + i + ":" + score.get(i), 0 + i * 25, 25);
            }

        } finally {
            if (g2d != null) {
                g2d.dispose();
            }
        }

        strategy.show();
    }

    public void mouseReleased(MouseEvent e) {
        Point tilePos = levelDrawer.mousePositionToTilePosition(getWidth(), getHeight(), e.getPoint());

        if (tilePos == null) {
            return;
        }

        Tile tile = level.tileAt((int) tilePos.getX(), (int) tilePos.getY());

        if (!Tile.isDoor(tile)) {
            return;
        }
        log.debug("Clicked on a door @" + tilePos.x + "," + tilePos.y);
        for (MouseCanvasListener listener : listeners) {
            listener.handleDoorClicked(new Door(tilePos, tile == Tile.DOOR_CLOSED ? false : true));
        }
    }

    public void mouseClicked(MouseEvent e) {
        //ignore
    }

    public void mousePressed(MouseEvent e) {
        //ignore
    }

    public void mouseEntered(MouseEvent e) {
        //ignore
    }

    public void mouseExited(MouseEvent e) {
        //ignore
    }

    public void handleUpdateMice(ArrayList<Mouse> mice) {
        if (level == null && !isVisible()) {
            log.debug("Discarded mice update: No Level");
            return;
        }
        for (Mouse m : mice) {
            if (!score.containsKey(m.getPlayerIndex())) {
                score.put(m.getPlayerIndex(), 0);
            }
        }
        //Update the data structure
        level.handleUpdateMice(mice);
        //Update the visuals
        renderFrame();
    }

    public void handleUpdateDoors(ArrayList<Door> doors) {
        if (level == null && !isVisible()) {
            log.debug("Discarded door update: No Level");
            return;
        }
        //Update the data structure
        level.handleUpdateDoors(doors);
        //Update the visuals
        renderFrame();
    }

    public void handleGameOver() {
        log.debug("Game over");
        JOptionPane.showMessageDialog(null, "Game over");
    }

    public void handleUpdateLevel(LevelStructure level) {

        if (this.level == null) {
            log.debug("Set initial level");
            this.level = new ClientLevel(level.getTiles(), level.getBaitPosition(), level.getStartPositions());
            levelDrawer = new LevelDrawer(this.level);
        } else {
            log.debug("Update level");
            this.level.handleUpdateLevel(level);
        }
        repaint();
    }

    public void handleGameStart() {
        log.debug("Game started");
        //JOptionPane.showMessageDialog(null, "GO!");
    }

    public void handleGameOver(int winner) {
        //Possibly change canvas appearence on victory
        //JOptionPane.showMessageDialog(null, "Player:" + winner + " has won the game");
        if (!score.containsKey(winner)) {
            score.put(winner, 0);
        }
        score.put(winner, score.get(winner) + 1);
    }

}
