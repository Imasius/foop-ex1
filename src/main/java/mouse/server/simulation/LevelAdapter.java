package mouse.server.simulation;

import mouse.shared.Orientation;
import mouse.shared.LevelStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides some convenience methods for Level User: Markus Date: 08.04.14
 */
public class LevelAdapter {

    private static final Logger log = LoggerFactory.getLogger(LevelAdapter.class);

    private final LevelStructure levelStructure;

    public LevelAdapter(LevelStructure levelStructure) {
        this.levelStructure = levelStructure;
    }

    public LevelStructure getLevelStructure() {
        return levelStructure;
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
