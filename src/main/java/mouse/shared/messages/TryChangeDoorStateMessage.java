package mouse.shared.messages;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by Florian on 2014-04-13.
 */
public class TryChangeDoorStateMessage extends Message implements Serializable {
    private Point doorPosition;
    private boolean tryClose;

    public TryChangeDoorStateMessage(Point doorPosition, boolean tryClose) {
        this.doorPosition = doorPosition;
        this.tryClose = tryClose;
    }

    public Point getDoorPosition() {
        return doorPosition;
    }
    public boolean tryClose() {
        return tryClose;
    }

    @Override
    public void alertListeners(Iterable<? extends MessageListener> observers) {
        for (MessageListener observer : observers)
            observer.onTryChangeDoorState(doorPosition, tryClose);
    }
}
