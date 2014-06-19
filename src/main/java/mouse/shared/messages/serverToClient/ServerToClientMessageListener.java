package mouse.shared.messages.serverToClient;

import java.util.ArrayList;
import mouse.shared.Door;
import mouse.shared.LevelStructure;
import mouse.shared.Mouse;

/**
 * The ServerToClientMessageListener listens for messages from server to client
 *
 * @author kevin_000
 */
public interface ServerToClientMessageListener {

    //One or several mice have changed
    void handleUpdateMice(ArrayList<Mouse> mice);

    //One or several doors have changed
    void handleUpdateDoors(ArrayList<Door> doors);

    //The level has changed
    void handleUpdateLevel(LevelStructure level);

    //A new game has started
    void handleGameStart();

    //Game is over
    void handleGameOver(int winner);
}
