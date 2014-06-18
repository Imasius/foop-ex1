package mouse.server.simulation;

import mouse.shared.Orientation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Represents a SimulationMouse in the simulation component. User: Markus Date:
 * 02.04.14
 */
public class SimulationMouse extends mouse.shared.Mouse {

    private Point position;
    private MouseBehaviour state;
    private LevelAdapter levelAdapter;
    private int randomCounter;
    private Orientation randomDirection;
    private Orientation lastOrientation = Orientation.NORTH;
    private int playerIndex;

    private static final Logger log = LoggerFactory.getLogger(SimulationMouse.class);

    public SimulationMouse(Point position, LevelAdapter level, int playerIndex) {
        log.debug("Mouse #{} created at {}.", playerIndex, position);
        state = MouseBehaviour.MOVING_DIRECTED;
        this.position = position;
        this.levelAdapter = level;
        this.playerIndex = playerIndex;
    }

    public Orientation move() {
        Point bait = levelAdapter.getLevelStructure().getBaitPosition();
        Orientation ret = null;
        switch (state) {
            case MOVING_DIRECTED:
                for (Orientation o : OrientationHelper.getInstance().getViableOrientations(position, bait)) {
                    if (levelAdapter.isDirectionFeasible(position, o)) {
                        ret = o;
                        break;
                    }
                }

                confuse();
                ret = randomDirection;
                break;
            case MOVING_RANDOM:
                if (!levelAdapter.isDirectionFeasible(position, randomDirection)) {
                    randomDirection = levelAdapter.getRandomFeasibleDirection(position);
                }
                ret = randomDirection;
                randomCounter--;

                if (randomCounter <= 0) {
                    state = MouseBehaviour.MOVING_DIRECTED;
                }
                break;
        }

        lastOrientation = ret;
        return ret;
    }

    public void applyMotion(Orientation direction) {
        position = OrientationHelper.getInstance().applyOrientation(position, direction);
    }

    public Point getPosition() {
        return position;
    }

    /**
     * It is possible that a subset of mice is transferred, a index is necessary
     * therefore to know which mouse to update
     *
     * @return the player number of the mouse.
     */
    public int getIndex() {
        return playerIndex;
    }

    public void confuse() {
        state = MouseBehaviour.MOVING_RANDOM;
        randomDirection = levelAdapter.getRandomFeasibleDirection(position);
        randomCounter = 4;
    }

    public Orientation getLastOrientation() {
        return lastOrientation;
    }
}
