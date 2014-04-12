package mouse.client.gui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

import mouse.client.data.ClientLevel;

public class MouseCanvas extends Canvas {

    private BufferStrategy strategy;
    private LevelDrawer levelDrawer;
    private ClientLevel level;

    //TODO set IgnoreRepaint to true
    public MouseCanvas() {
        level = new ClientLevel();
        levelDrawer = new LevelDrawer(level);
        setIgnoreRepaint(false);
    }

    public ClientLevel getLevel() {
        return level;
    }

    public void init() {
        setVisible(true);
        createBufferStrategy(2);
        strategy = getBufferStrategy();
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