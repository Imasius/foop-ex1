package mouse.server.network.event;

import java.awt.Point;
import mouse.shared.Door;

/**
 * DoorsChangedEvent which should be triggered when a door changes.
 *
 * It is likely that multiple doors change at once and that their new status can
 * be propagated together instead of several messages
 *
 * @author kevin_000
 */
public class DoorsChangedEvent {

    private Door door;

    public DoorsChangedEvent(Door door) {
        this.door = door;
    }

    /**
     * @return the door
     */
    public Door getDoor() {
        return door;
    }

    /**
     * @param door the door to set
     */
    public void setDoor(Door door) {
        this.door = door;
    }

}
