package mouse.server.network.event;

import java.util.ArrayList;
import java.util.List;
import mouse.server.simulation.Mouse;

/**
 * MiceChangedEvent which should be triggered whenever the state any mouse
 * changes. It is very likely that multiple mice change at once and that their
 * changes can be propagated at once.
 *
 * @author kevin_000
 */
public class MiceChangedEvent {

    private List<Mouse> mice;

    public MiceChangedEvent(List<Mouse> mice) {
        this.mice = mice;
    }

    /**
     * @return the mice
     */
    public List<Mouse> getMice() {
        return mice;
    }

    /**
     * @param mice the mice to set
     */
    public void setMice(List<Mouse> mice) {
        this.mice = mice;
    }

}
