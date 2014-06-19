package mouse.server.level;

import java.awt.*;
import java.util.ArrayList;
import mouse.shared.LevelStructure;
import mouse.shared.Tile;

/**
 * User: Simon Date: 16.06.2014
 */
public class FileLevel extends LevelStructure {

    public static final char WALL = 'X';
    public static final char EMPTY = ' ';
    public static final char DOOR = 'D';
    public static final char START = 'S';
    public static final char BAIT = 'B';

    private int height;
    private int width;

    public FileLevel() {
    }

    public FileLevel(Tile[][] tiles, Point baitPosition, ArrayList<Point> startPositions) {
        super(tiles, baitPosition, startPositions);
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public Tile tileAt(int x, int y) {
        return tiles[x][y];
    }

    public void setTileAt(int x, int y, Tile tile) {
        tiles[x][y] = tile;
    }

    @Override
    public Point getBaitPosition() {
        return baitPosition;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public void setStartPositions(ArrayList<Point> startPositions) {
        this.startPositions = startPositions;
    }

    public void setBaitPosition(Point baitPosition) {
        this.baitPosition = baitPosition;
    }
}
