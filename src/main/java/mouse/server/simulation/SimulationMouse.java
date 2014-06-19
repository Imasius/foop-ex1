package mouse.server.simulation;

import mouse.shared.Orientation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import mouse.shared.Mouse;

/**
 * Changed from simple Mouse/MouseState to SimulationMouse. A SimulationMouse
 * extends the shareable data structure of Mouse with Behaviour.
 *
 * Represents a SimulationMouse in the simulation component. User: Markus Date:
 * 02.04.14
 */
public class SimulationMouse extends Mouse {

    private static final Logger log = LoggerFactory.getLogger(SimulationMouse.class);

    private MouseBehaviour state;
    private int randomCounter;

    public SimulationMouse(Point position, Orientation orientation, int playerIndex) {
        super(position, orientation, playerIndex);
        log.debug("Mouse #{} created at {} and looks towards {}.", playerIndex, position, orientation);
        state = MouseBehaviour.MOVING_DIRECTED;
    }

    public boolean isConfused() {
        return randomCounter > 0;
    }

    public boolean isSniffing() {
        return randomCounter > 2;
    }

    /**
     * While i mouse is confused it will only sniff
     */
    public void sniff() {
        randomCounter = Math.max(randomCounter - 1, 0);
        if (randomCounter == 0) {
            //Done sniffing
            state = MouseBehaviour.MOVING_DIRECTED;
        }
    }

    public void move() {
        applyMotion(getOrientation());
    }

    private void applyMotion(Orientation direction) {
        position = OrientationHelper.getInstance().applyMotion(position, direction);
    }

    public MouseBehaviour getBehaviour() {
        return state;
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
        int rand = 1 + (int) (Math.random() * 2);
        randomCounter = rand;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

}
