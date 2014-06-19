package mouse.server.network.event;

import java.util.ArrayList;
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

    private ArrayList<Door> doors;

    public DoorsChangedEvent(ArrayList<Door> doors) {
        this.doors = doors;
    }

    /**
     * @return the door
     */
    public ArrayList<Door> getDoors() {
        return doors;
    }
}
