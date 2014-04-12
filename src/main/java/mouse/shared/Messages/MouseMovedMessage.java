package mouse.shared.Messages;

import java.awt.*;
import java.io.*;

/**
 * Created by Florian on 2014-04-12.
 */
public class MouseMovedMessage extends Message implements Serializable {
    private int mouseIdx;
    private Point newPosition;

    public MouseMovedMessage(int mouseIdx, Point newPosition) {
        this.mouseIdx = mouseIdx;
        this.newPosition = newPosition;
    }

    public int getMouseIdx() {
        return mouseIdx;
    }
    public Point getNewPosition() {
        return newPosition;
    }

    @Override
    protected void alertObservers(Iterable<MessageObserver> observers) {
        for (MessageObserver observer : observers)
            observer.onMouseMoved(mouseIdx, newPosition);
    }
}
