package mouse.server.network.event;

import java.util.ArrayList;
import java.util.List;
import mouse.server.simulation.SimulationMouse;
import mouse.shared.Mouse;

/**
 * MiceChangedEvent which should be triggered whenever the state any mouse
 * changes. It is very likely that multiple mice change at once and that their
 * changes can be propagated at once.
 *
 * @author kevin_000
 */
public class MiceChangedEvent {

    private ArrayList<Mouse> mice;

    public MiceChangedEvent(ArrayList<Mouse> mice) {
        this.mice = mice;
    }

    /**
     * @return the mice
     */
    public ArrayList<Mouse> getMice() {
        return mice;
    }
}
