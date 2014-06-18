package mouse.shared.messages.serverToClient;

import java.util.ArrayList;
import mouse.server.simulation.SimulationMouse;

/**
 *
 * @author kevin_000
 */
public class UpdateMiceMessage extends ServerToClientMessage {

    private final ArrayList<SimulationMouse> mice;

    public UpdateMiceMessage(ArrayList<SimulationMouse> mice) {
        this.mice = mice;
    }

    public ArrayList<SimulationMouse> getMice() {
        return mice;
    }

    @Override
    public void alertListeners(Iterable<? extends ServerToClientMessageListener> observers) {
        for (ServerToClientMessageListener observer : observers) {
            observer.handleUpdateMice(mice);
        }
    }
}
