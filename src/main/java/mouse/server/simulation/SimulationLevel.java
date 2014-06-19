package mouse.server.simulation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import mouse.shared.LevelStructure;
import mouse.shared.Orientation;
import mouse.shared.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Simon Date: 17.06.2014
 */
public class SimulationLevel extends LevelStructure {

    private static final Logger log = LoggerFactory.getLogger(SimulationLevel.class);

    public SimulationLevel(LevelStructure level) {
        this.tiles = level.getTiles();
        this.startPositions = level.getStartPositions();
        this.baitPosition = level.getBaitPosition();
    }

    public void setBaitPosition(int x, int y) {
        baitPosition.x = x;
        baitPosition.y = y;
    }

    public void openDoor(Point doorPosition) {
        tiles[doorPosition.x][doorPosition.y] = Tile.DOOR_OPEN;
    }

    public void closeDoor(Point doorPosition) {
        tiles[doorPosition.x][doorPosition.y] = Tile.DOOR_CLOSED;
    }

    public boolean isDirectionFeasible(Point position, Orientation direction) {
        Point checkedPosition = OrientationHelper.getInstance().applyMotion(position, direction);

        if (checkedPosition.x < 0 || checkedPosition.y < 0
                || checkedPosition.x >= getWidth() || checkedPosition.y >= getHeight()) {
            //throw new RuntimeException("asd");
            log.debug("CheckedPosition is out of bounds:" + position.getX() + "," + position.getY() + " width " + getWidth() + " height" + getHeight());
            return false;
        }

        switch (tileAt(checkedPosition.x, checkedPosition.y)) {
            case DOOR_CLOSED:
                //log.debug("Closed");
                return false;
            case WALL:
                //log.debug("Wall");
                return false;
            case DOOR_OPEN:
                //log.debug("Open");
                return true;
            case EMPTY:
                //log.debug("Empty");
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
