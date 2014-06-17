package mouse.shared.messages.serverToClient;

import java.util.ArrayList;
import mouse.shared.Door;

/**
 *
 * @author Kevin Streicher <e1025890@student.tuwien.ac.at>
 */
public class UpdateDoorsMessage extends ServerToClientMessage {

    private final ArrayList<Door> doors;

    public UpdateDoorsMessage(ArrayList<Door> doors) {
        this.doors = doors;
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }

    @Override
    public void alertListeners(Iterable<? extends ServerToClientMessageListener> observers) {
        for (ServerToClientMessageListener observer : observers) {
            observer.handleUpdateDoors(doors);
        }
    }
}
