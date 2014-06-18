package mouse.server.level;

import mouse.server.simulation.SimulationLevel;
import mouse.shared.LevelStructure;
import mouse.shared.Tile;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Simon
 * Date: 17.06.2014
 */
public class LevelLoader {

    /**
     * Will load a level for the mouse game from file.
     * @param level Path to the level which should be loaded.
     * @return Parsed level.
     * @throws mouse.server.level.LevelException if the file cannot be found or contains illegal characters.
     */
    public static SimulationLevel loadLevel(String level) {
        List<String> levelAsLines = loadLines(level);
        return new SimulationLevel(createLevel(levelAsLines));
    }

    private static LevelStructure createLevel(List<String> levelAsLines) {
        FileLevel level = new FileLevel();
        level.setHeight(levelAsLines.size());
        level.setWidth(levelAsLines.get(0).length());
        level.setTiles(new Tile[level.getWidth()][level.getHeight()]);

        parseLines(level, levelAsLines);

        return level;
    }

    private static void parseLines(FileLevel level, List<String> levelAsLines) {
        int x = 0;
        int y = 0;
        List<Point> startPositions = new ArrayList<Point>();

        for (String tileLine : levelAsLines) {
            for (char tile : tileLine.toCharArray()) {
                level.setTileAt(x, y, charTileToEnum(tile));

                if (tile == FileLevel.START) {
                    startPositions.add(new Point(x, y));
                } else if (tile == FileLevel.BAIT) {
                    level.setBaitPosition(new Point(x, y));
                }
                x++;
            }
            x = 0;
            y++;
        }

        level.setStartPositions(startPositions);
    }

    private static Tile charTileToEnum(char tile) {
        switch(tile) {
            case FileLevel.WALL: return Tile.WALL;
            case FileLevel.EMPTY: return Tile.EMPTY;
            case FileLevel.DOOR: return Tile.DOOR_OPEN;
            case FileLevel.START: return Tile.EMPTY;
            case FileLevel.BAIT: return Tile.EMPTY;
            default:
                throw new LevelException("Illegal character in level description: " + tile);
        }
    }

    private static List<String> loadLines(String level) {
        try {
            Reader isr = new InputStreamReader(LevelLoader.class.getClassLoader().getResourceAsStream(level));
            BufferedReader reader = new BufferedReader(isr);
            String line;
            List<String> levelAsLines = new ArrayList<String>();
            while ((line = reader.readLine()) != null) {
                levelAsLines.add(line);
            }
            reader.close();

            return levelAsLines;
        } catch (IOException ex) {
            throw new LevelException("Error reading level.", ex);
        }
    }
}
