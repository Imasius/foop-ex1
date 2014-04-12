package mouse.shared.Messages;

import mouse.shared.Level;

import java.awt.*;

/**
 * Created by Florian on 2014-04-12.
 */
public interface MessageObserver {
    void onMouseMoved(int mouseIdx, Point newPosition);
    void onDoorStateChanged(Point doorPosition, boolean isClosed);
    void onGameOver();
    void onGameStart(Level level);
}
