package mouse.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import mouse.server.simulation.Orientation;
import mouse.shared.Level;
import mouse.shared.Pair;
import mouse.shared.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date 06.04.2014
 *
 * @author Kevin Streicher <e1025890@student.tuwien.ac.at>
 */
public class DrawableLevel implements Drawable {

    private static final Logger log = LoggerFactory.getLogger(DrawableLevel.class);
    //TODO fix visibility
    Level level;
    public int width;
    public int height;

    public DrawableLevel(Level level) {
        this.level = level;
    }

    public void draw(Graphics2D g2d) {
        //Draw all tiles (~Background)        
        g2d.setColor(Color.blue);
        g2d.drawLine(20 * level.getHeight(), 0, 20 * level.getWidth(), 20 * level.getHeight());
        g2d.drawLine(0, 0, 20 * level.getWidth(), 0);
        g2d.drawLine(0, 0, 0, 20 * level.getHeight());
        g2d.drawLine(20 * level.getWidth(), 0, 20 * level.getWidth(), 20 * level.getWidth());

        //TODO: Remove dummy draw
        for (int x = 0; x < level.getWidth(); x++) {
            for (int y = 0; y < level.getHeight(); y++) {
                Tile t = level.tileAt(x, y);
                g2d.setColor(Color.pink);
                g2d.drawRect(x * 20, y * 20, 20, 20);
                //TODO: draw Tile
                //TODO replace with image loading and image drawing
                switch (t) {
                    case EMPTY:
                        g2d.setColor(Color.LIGHT_GRAY);
                        break;
                    case DOOR_CLOSED:
                        g2d.setColor(Color.ORANGE);
                        break;
                    case DOOR_OPEN:
                        g2d.setColor(Color.GREEN);
                        break;
                    case WALL:
                        g2d.setColor(Color.DARK_GRAY);
                        break;
                }
                g2d.fillRect(x * 20 + 1, y * 20 + 1, 19, 19);
            }
        }
        //Draw startpositions
        for (Point p : level.getStartpositions()) {
            //TODO: draw startpositions
        }
        //Draw Bait
        level.getBaitPosition();
        //TODO: drawBait

        //Draw mice
        g2d.setColor(Color.GREEN);
        for (Pair<Point, Orientation> m : level.getMousePosition()) {
            int x = m.first.x;
            int y = m.first.y;
            //TODO: draw Tile
            //TODO replace with image loading and image drawing
            switch (m.second) {
                case EAST:
                    g2d.fillRect(x * 20, y * 20 + 5, 15, 10);
                    g2d.fillOval(x * 20 + 15, y * 20 + 7, 5, 5);
                    break;
                case NORTH:
                    g2d.fillRect(x * 20 + 5, y * 20 + 5, 10, 15);
                    g2d.fillOval(x * 20 + 7, y * 20, 5, 5);
                    break;
                case SOUTH:
                    g2d.fillRect(x * 20 + 5, y * 20, 10, 15);
                    g2d.fillOval(x * 20 + 7, y * 20 + 15, 5, 5);
                    break;
                case WEST:
                    g2d.fillRect(x * 20 + 5, y * 20 + 5, 15, 10);
                    g2d.fillOval(x * 20, y * 20 + 7, 5, 5);
                    break;
            }
        }
    }

}
