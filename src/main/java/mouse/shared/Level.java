package mouse.shared;

import mouse.server.simulation.Mouse;
import mouse.shared.messages.GameStartMessage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Simon
 * Date: 17.06.2014
 */
public class Level implements LevelStructure {

    private final LevelStructure levelStructure;
    private final List<Mouse> mice = new ArrayList<Mouse>();

    public Level(LevelStructure levelStructure) {
        this.levelStructure = levelStructure;
    }

    public void openDoor(Point doorPosition) {
        levelStructure.setTileAt(doorPosition.x, doorPosition.y, Tile.DOOR_OPEN);
    }

    public void closeDoor(Point doorPosition) {
        levelStructure.setTileAt(doorPosition.x, doorPosition.y, Tile.DOOR_CLOSED);
    }

    public void addMouse(Mouse mouse) {
        mice.add(mouse);
    }

    @Override
    public int getHeight() {
        return levelStructure.getHeight();
    }

    @Override
    public int getWidth() {
        return levelStructure.getWidth();
    }

    @Override
    public Tile tileAt(int x, int y) {
        return levelStructure.tileAt(x, y);
    }

    @Override
    public void setTileAt(int x, int y, Tile tile) {
        levelStructure.setTileAt(x, y, tile);
    }

    @Override
    public Point getBaitPosition() {
        return levelStructure.getBaitPosition();
    }

    @Override
    public Collection<Point> getStartPositions() {
        return levelStructure.getStartPositions();
    }

    @Override
    public GameStartMessage toGameStartMessage() {
        return levelStructure.toGameStartMessage();
    }

    public List<Mouse> getMice() {
        return mice;
    }
}
