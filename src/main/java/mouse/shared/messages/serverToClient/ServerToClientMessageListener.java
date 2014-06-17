package mouse.shared.messages.serverToClient;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import mouse.server.simulation.Mouse;
import mouse.shared.Door;
import mouse.shared.MouseState;
import mouse.shared.Tile;

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

    //A new game has started
    void handleGameStart(Tile[][] tiles, Point baitPosition, Collection<Point> startPositions, Collection<MouseState> mice);

    //Game is over
    void handleGameOver();
}
