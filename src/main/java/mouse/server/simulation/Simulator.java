package mouse.server.simulation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mouse.shared.Orientation;
import mouse.shared.Mouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simulates the Game Logic. User: Markus Date: 05.05.14
 */
public class Simulator {

    private static final Logger log = LoggerFactory.getLogger(Simulator.class);

    public Simulator() {
    }

    /**
     * simulate(..) does one simulation step and returns all mice where
     * something has changed
     */
    public ArrayList<Mouse> simulate(List<SimulationMouse> mice, SimulationLevel level) {
        Set<SimulationMouse> confusedMice = new HashSet<SimulationMouse>();
        ArrayList<Mouse> movedMice = new ArrayList<Mouse>();

        for (SimulationMouse mouse : mice) {
            if (mouse.isConfused()) {
                mouse.sniff();
                if (mouse.isSniffing()) {
                    //if the timer is to high we wont move, but else we will choose a random direciton
                    continue;
                }

            }
            if(mouse.getBehaviour() == MouseBehaviour.MOVING_DIRECTED){
                mouse.setOrientation(getNewDirection(mouse, level));
            }
            //On each update get a new direction
            if (level.isDirectionFeasible(mouse.getPosition(), mouse.getOrientation())) {
                Point currentPosition = mouse.getPosition();
                mouse.move();
                boolean bumpedIntoAMouse = false;
                for (SimulationMouse other : mice) {
                    if (mouse.equals(other)) {
                        continue;
                    }
                    //Did we bump into another mouse?
                    if (mouse.getPosition().equals(other.getPosition())) {
                        mouse.confuse();
                        other.confuse();
                        bumpedIntoAMouse = true;
                        break;
                    }
                }
                if (bumpedIntoAMouse) {
                    mouse.setPosition(currentPosition);
                } else {
                    //We moved and it was a valid move
                    movedMice.add(mouse);
                }
            } else {
                mouse.setOrientation(level.getRandomFeasibleDirection(mouse.getPosition()));
                //We did not move but we changed our direction
                movedMice.add(mouse);
            }

        }

        return movedMice;
    }

    public Orientation getNewDirection(SimulationMouse mouse, SimulationLevel level) {
        switch (mouse.getBehaviour()) {
            case MOVING_DIRECTED:
                //Get Directions pointing to the bait
                List<Orientation> allowed = OrientationHelper.getInstance().getViableOrientations(mouse.getPosition(), level.getBaitPosition());
                Collections.shuffle(allowed);//getViableOrientions does return orientation pointing to bait, if there are 2, take a random one
                for (Orientation o : allowed) {
                    //If any viable direction is found return it
                    if (level.isDirectionFeasible(mouse.getPosition(), o)) {
                        return o;
                    }
                }
                //If no feasible direction is found -> confuse the mouse
                mouse.confuse();
                return level.getRandomFeasibleDirection(mouse.getPosition());
            case MOVING_RANDOM:
                if (!level.isDirectionFeasible(mouse.getPosition(), mouse.getOrientation())) {
                    return level.getRandomFeasibleDirection(mouse.getPosition());
                }
                //Stay like this
                return mouse.getOrientation();
        }
        throw new RuntimeException("Illegal Gamestate! There is no feasible direction for this mouse!" + mouse.getPosition().getX() + "," + mouse.getPosition().getY());
    }
}
