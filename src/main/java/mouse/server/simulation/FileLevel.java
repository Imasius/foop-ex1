package mouse.server.simulation;

import mouse.shared.Level;
import mouse.shared.MouseState;
import mouse.shared.Tile;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Simon
 * Date: 16.06.2014
 */
public class FileLevel implements Level {

    private int height;
    private int width;

    private Tile[][] tiles;

    public static final char WALL = 'X';
    public static final char EMPTY = ' ';
    public static final char DOOR = 'D';
    public static final char START = 'S';

    private List<Point> startPositions = new ArrayList<Point>();

    public FileLevel(File level) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(level));
        String line;
        List<String> levelAsLines = new ArrayList<String>();
        while ((line = reader.readLine()) != null) {
            levelAsLines.add(line);
        }
        reader.close();

        height = levelAsLines.size();
        width = levelAsLines.get(0).length();
        tiles = new Tile[height][width];

        int x = 0;
        int y = 0;

        for (String tileLine : levelAsLines) {
            for (char tile : tileLine.toCharArray()) {
                switch(tile) {
                    case WALL: tiles[y][x] = Tile.WALL; break;
                    case EMPTY: tiles[y][x] = Tile.EMPTY; break;
                    case DOOR: tiles[y][x] = Tile.DOOR_OPEN; break;
                    case START:
                        startPositions.add(new Point(x, y));
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal character in level description: " + tile);
                }
                x++;
            }
            x = 0;
            y++;
        }
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
        return tiles[y][x];
    }

    @Override
    public Point getBaitPosition() {
        return null;
    }

    @Override
    public Collection<Point> getStartPositions() {
        return startPositions;
    }

    @Override
    public Collection<MouseState> getMice() {
        return null;
    }
}
