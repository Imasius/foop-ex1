package mouse.server.simulation;

import mouse.server.simulation.SimulationMouse;
import mouse.shared.messages.serverToClient.GameStartMessage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import mouse.shared.LevelStructure;
import mouse.shared.Orientation;
import mouse.shared.Tile;

/**
 * User: Simon Date: 17.06.2014
 */
public class SimulationLevel implements LevelStructure {

    public SimulationLevel() {        
    }

    public void openDoor(Point doorPosition) {
        levelStructure.setTileAt(doorPosition.x, doorPosition.y, Tile.DOOR_OPEN);
    }

    public void closeDoor(Point doorPosition) {
        levelStructure.setTileAt(doorPosition.x, doorPosition.y, Tile.DOOR_CLOSED);
    }

    public void addMouse(SimulationMouse mouse) {
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

    public List<SimulationMouse> getMice() {
        return mice;
    }

    public boolean isDirectionFeasible(Point position, Orientation direction) {
        Point checkedPosition = OrientationHelper.getInstance().applyOrientation(position, direction);

        if (checkedPosition.x < 0 || checkedPosition.y < 0
                || checkedPosition.x >= levelStructure.getWidth() || checkedPosition.y >= levelStructure.getHeight()) {
            return false;
        }

        switch (levelStructure.tileAt(checkedPosition.x, checkedPosition.y)) {
            case DOOR_CLOSED:
                return false;
            case WALL:
                return false;
            case DOOR_OPEN:
                return true;
            case EMPTY:
                return true;
        }
        throw new IllegalArgumentException("Unsupported TileType!");
    }

    public Orientation getRandomFeasibleDirection(Point position) {
        List<Orientation> list = Arrays.asList(Orientation.values());
        Collections.shuffle(list);

        for (Orientation direction : list) {
            if (this.isDirectionFeasible(position, direction)) {
                return direction;
            }
        }
        throw new RuntimeException("Illegal Gamestate! Mouse wants to move but cannot!");
    }
}
