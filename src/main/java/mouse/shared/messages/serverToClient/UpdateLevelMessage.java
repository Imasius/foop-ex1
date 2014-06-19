package mouse.shared.messages.serverToClient;

import java.util.ArrayList;
import mouse.shared.Door;
import mouse.shared.LevelStructure;

/**
 *
 * @author Kevin Streicher <e1025890@student.tuwien.ac.at>
 */
public class UpdateLevelMessage extends ServerToClientMessage {

    private final LevelStructure level;

    public UpdateLevelMessage(LevelStructure level) {
        this.level = level;
    }

    public LevelStructure getLevel() {
        return level;
    }

    @Override
    public void alertListeners(Iterable<? extends ServerToClientMessageListener> observers) {
        for (ServerToClientMessageListener observer : observers) {
            observer.handleUpdateLevel(level);
        }
    }
}
