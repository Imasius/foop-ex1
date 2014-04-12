package mouse.client.network;

import mouse.shared.Level;

import java.awt.*;

/**
 * Created by Florian on 2014-04-12.
 */
public interface ServerConnectionObserver {
    void onMouseMoved(int mouseIdx, Point newPosition);
    void onDoorStateChanged(Point doorPosition, boolean isClosed);
    void onGameOver();
    void onGameStart(Level level);
}
