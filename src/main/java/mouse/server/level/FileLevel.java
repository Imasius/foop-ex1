package mouse.server.level;

import mouse.server.simulation.Mouse;
import mouse.shared.LevelStructure;
import mouse.shared.Mouse;
import mouse.shared.Tile;
import mouse.shared.messages.serverToClient.GameStartMessage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Simon
 * Date: 16.06.2014
 */
public class FileLevel implements LevelStructure {

    public static final char WALL = 'X';
    public static final char EMPTY = ' ';
    public static final char DOOR = 'D';
    public static final char START = 'S';
    public static final char BAIT = 'B';

    private int height;
    private int width;

    private Tile[][] tiles;

    private List<Point> startPositions = new ArrayList<Point>();
    private Point baitPosition;
    private List<Mouse> mice = new ArrayList<Mouse>();

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

    @Override
    public Collection<Point> getStartPositions() {
        return startPositions;
    }

    @Override
    public GameStartMessage toGameStartMessage() {
        ArrayList<Mouse> states = new ArrayList<Mouse>();
        for (Mouse m : mice) {
            states.add(new Mouse(m.getPosition(), m.getLastOrientation()));
        }

        return new GameStartMessage(tiles, baitPosition, startPositions, states);
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

    public void setStartPositions(List<Point> startPositions) {
        this.startPositions = startPositions;
    }

    public void setBaitPosition(Point baitPosition) {
        this.baitPosition = baitPosition;
    }
}
