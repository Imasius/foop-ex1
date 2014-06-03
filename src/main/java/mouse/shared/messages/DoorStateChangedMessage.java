package mouse.shared.messages;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by Florian on 2014-04-12.
 */
public class DoorStateChangedMessage extends Message implements Serializable {
    private Point doorPosition;
    private boolean isClosed;

    public DoorStateChangedMessage(Point doorPosition, boolean isClosed) {
        this.doorPosition = doorPosition;
        this.isClosed = isClosed;
    }

    public Point getDoorPosition() {
        return doorPosition;
    }
    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public void alertListeners(Iterable<? extends MessageListener> observers) {
        for (MessageListener observer : observers)
            observer.onDoorStateChanged(doorPosition, isClosed);
    }
}
