package mouse.client.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.*;
import java.util.List;

import mouse.client.data.ClientLevel;
import mouse.shared.Tile;

public class MouseCanvas extends Canvas {
    class LocalMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            Point tilePos = levelDrawer.mousePositionToTilePosition(getWidth(), getHeight(), e.getPoint());

            if (tilePos == null)
                return;

            Tile tile = level.tileAt((int)tilePos.getX(), (int)tilePos.getY());

            if (!Tile.isDoor(tile))
                return;

            for(MouseCanvasListener listener : listeners)
                listener.onDoorClicked(tilePos, tile == Tile.DOOR_CLOSED);
        }

        @Override public void mousePressed(MouseEvent e) { } // ignored
        @Override public void mouseReleased(MouseEvent e) { } // ignored
        @Override public void mouseEntered(MouseEvent e) { } // ignored
        @Override public void mouseExited(MouseEvent e) { } // ignored
    }

    private BufferStrategy strategy;
    private final LevelDrawer levelDrawer;
    private final ClientLevel level;
    private final List<MouseCanvasListener> listeners;



    //TODO set IgnoreRepaint to true
    public MouseCanvas() {
        this.level = new ClientLevel();
        this.levelDrawer = new LevelDrawer(this.level);
        this.listeners = new ArrayList<MouseCanvasListener>();
        setIgnoreRepaint(false);

        this.addMouseListener(new LocalMouseListener());
    }

    public ClientLevel getLevel() {
        return this.level;
    }


    public void addListener(MouseCanvasListener listener){
        this.listeners.add(listener);
    }
    public void removeListener(MouseCanvasListener listener){
        this.listeners.remove(listener);
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
    private void renderFrame(){
        Graphics2D g2d = null;
        try {
            g2d = (Graphics2D) strategy.getDrawGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                 RenderingHints.VALUE_ANTIALIAS_ON);

            DrawArgs args = new DrawArgs(getWidth(),
                                         getHeight(),
                                         g2d);

            levelDrawer.draw(args);

            /*
             double fps = ((int) GameLogic.fps * 100) / 100;
             String message = "FPS:" + fps;
             g.drawString(message, 0, 20);

             if (SpeedBowlGame.displayedGame != null) {
             if (SpeedBowlGame.displayedGame.logic.state == GameLogic.State.READY) {
             message = "READY";
             g.setFont(new Font("Calibri", Font.PLAIN, 30));
             g.drawString(message, getWidth() / 2 - 30, getHeight() / 2 - 30);
             }
             }
             */
        }
        finally{
            if (g2d != null) g2d.dispose();
        }

        strategy.show();
    }
}