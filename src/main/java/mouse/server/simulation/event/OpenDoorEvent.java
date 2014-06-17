package mouse.server.simulation.event;

import java.awt.Point;

/**
 *
 * @author Kevin Streicher <e1025890@student.tuwien.ac.at>
 */
public class OpenDoorEvent {

    public Point doorPosition;

    public OpenDoorEvent(Point doorPosition) {
        this.doorPosition = doorPosition;
    }

}
