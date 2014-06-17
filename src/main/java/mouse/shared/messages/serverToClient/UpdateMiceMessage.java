package mouse.shared.messages.serverToClient;

import java.util.ArrayList;
import mouse.server.simulation.Mouse;

/**
 *
 * @author kevin_000
 */
public class UpdateMiceMessage extends ServerToClientMessage {

    private final ArrayList<Mouse> mice;

    public UpdateMiceMessage(ArrayList<Mouse> mice) {
        this.mice = mice;
    }

    @Override
    public void alertListeners(Iterable<? extends ServerToClientMessageListener> observers) {
        for (ServerToClientMessageListener observer : observers) {
            observer.handleUpdateMice(mice);
        }
    }
}
