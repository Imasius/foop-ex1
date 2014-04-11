package mouse.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.Collection;
import java.util.LinkedList;
import mouse.server.simulation.Orientation;
import mouse.shared.Level;
import mouse.shared.Pair;
import mouse.shared.Tile;

public class MouseCanvas extends Canvas {

    private BufferStrategy strategy;
    private DrawableLevel level;

    {//TODO Remove mockup initializer object
        level = new DrawableLevel(new Level() {
            Tile[][] t = new Tile[20][20];

            {
                for (int i = 0; i < 20; i++) {
                    for (int j = 0; j < 20; j++) {
                        t[i][j] = Tile.EMPTY;
                    }
                }
                for (int i = 0; i < 20; i++) {
                    t[0][i] = Tile.WALL;
                    t[i][0] = Tile.WALL;
                    t[19][i] = Tile.WALL;
                    t[i][19] = Tile.WALL;
                }

                t[4][4] = Tile.DOOR_CLOSED;
                t[10][4] = Tile.DOOR_OPEN;

            }

            public int getHeight() {
                return 20;
            }

            public int getWidth() {
                return 20;
            }

            public Tile tileAt(int x, int y) {
                return t[x][y];
            }

            public Point getBaitPosition() {
                return new Point(5, 5);
            }

            public Collection<Point> getStartpositions() {
                Collection<Point> c = new LinkedList<Point>();
                c.add(new Point(2, 2));
                c.add(new Point(4, 4));
                c.add(new Point(6, 6));
                c.add(new Point(8, 8));
                return c;
            }

            public Collection<Pair<Point, Orientation>> getMousePosition() {
                Collection<Pair<Point, Orientation>> c = new LinkedList<Pair<Point, Orientation>>();
                c.add(new Pair<Point, Orientation>(new Point(7, 1), Orientation.EAST));
                c.add(new Pair<Point, Orientation>(new Point(7, 2), Orientation.NORTH));
                c.add(new Pair<Point, Orientation>(new Point(7, 3), Orientation.WEST));
                c.add(new Pair<Point, Orientation>(new Point(7, 4), Orientation.SOUTH));
                return c;
            }

        });
    }

    //TODO set IgnoreRepaint to true
    public MouseCanvas() {
        setIgnoreRepaint(false);
    }

    public MouseCanvas(DrawableLevel level) {
        setIgnoreRepaint(false);
        this.level = level;
    }


    public void setLevel(DrawableLevel level) {
        this.level = level;
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

            level.draw(args);

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