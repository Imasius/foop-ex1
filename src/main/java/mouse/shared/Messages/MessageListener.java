package mouse.shared.Messages;

import mouse.server.simulation.Orientation;
import mouse.shared.Level;
import mouse.shared.MouseState;
import mouse.shared.Pair;
import mouse.shared.Tile;

import java.awt.*;
import java.util.Collection;

/**
 * Created by Florian on 2014-04-12.
 */
public interface MessageListener {
    void onMouseMoved(int mouseIdx, MouseState newState);
    void onDoorStateChanged(Point doorPosition, boolean isClosed);
    void onGameOver();
    void onTryChangeDoorState(Point doorPosition, boolean tryClose);
    void onGameStart(Tile[][] tiles, Point baitPosition, Collection<Point> startPositions, Collection<MouseState> mice);
}
