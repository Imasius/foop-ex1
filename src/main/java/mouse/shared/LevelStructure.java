package mouse.shared;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Abstract Level to be used on both Server and Client. Coordinates start at
 * (0,0) in the upper-left corner and range to (width-1, height-1). User: Markus
 * Date: 02.04.14
 *
 * Added Collection<Pair<Point, Orientation>> getMousePosition() user: Kevin
 * Date: 06.04.14
 */
public abstract class LevelStructure implements Serializable {

    private Tile[][] tiles;
    private Point baitPosition;
    //ArrayList is serializeable!
    private ArrayList<Point> startPosition;

    public LevelStructure(Tile[][] tiles) {
        this.tiles = tiles;
    }

    abstract int getHeight();

    public abstract int getWidth();

    public abstract Tile tileAt(int x, int y);

    public Point getBaitPosition() {
        return baitPosition;
    }

    public ArrayList<Point> getStartPositions() {
        return startPosition;
    }
}
