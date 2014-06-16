package mouse.server.event;

import java.awt.Point;

/**
 *
 * @author Kevin Streicher <e1025890@student.tuwien.ac.at>
 */
public class CloseDoorEvent {

    public Point doorPosition;

    public CloseDoorEvent(Point doorPosition) {
        this.doorPosition = doorPosition;
    }
}
