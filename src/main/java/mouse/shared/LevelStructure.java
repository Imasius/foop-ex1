package mouse.shared;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Abstract Level to be used on both Server and Client. Coordinates start at
 * (0,0) in the upper-left corner and range to (width-1, height-1). User: Markus
 * Date: 02.04.14
 *
 * Updated to better ensure information for both server and client is defined in
 * this class
 *
 */
public abstract class LevelStructure implements Serializable {

    protected Tile[][] tiles;
    protected Point baitPosition;
    //ArrayList is serializeable!
    protected ArrayList<Point> startPositions;

    public LevelStructure() {

    }

    public LevelStructure(Tile[][] tiles, Point baitPosition, ArrayList<Point> startPosition) {
        this.tiles = tiles;
        this.baitPosition = baitPosition;
        this.startPositions = startPosition;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public int getHeight() {
        return tiles[0].length;
    }

    public int getWidth() {
        return tiles.length;
    }

    public Tile tileAt(int x, int y) {
        return tiles[x][y];
    }

    public Point getBaitPosition() {
        return baitPosition;
    }

    public ArrayList<Point> getStartPositions() {
        return startPositions;
    }
}
