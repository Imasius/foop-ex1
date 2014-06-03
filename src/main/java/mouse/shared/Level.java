package mouse.shared;

import java.awt.Point;
import java.util.Collection;

/**
 * Abstract Level to be used on both Server and Client. Coordinates start at
 * (0,0) in the upper-left corner and range to (width-1, height-1). User: Markus
 * Date: 02.04.14
 *
 * Added Collection<Pair<Point, Orientation>> getMousePosition() user: Kevin Date: 06.04.14
 */
public interface Level {

    public int getHeight();

    public int getWidth();

    public Tile tileAt(int x, int y);

    public Point getBaitPosition();

    public Collection<Point> getStartPositions();

    public Collection<MouseState> getMice();
}
