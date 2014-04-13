package mouse.shared.Messages;

import mouse.shared.MouseState;

import java.io.*;

/**
 * Created by Florian on 2014-04-12.
 */
public class MouseMovedMessage extends Message implements Serializable {
    private int mouseIdx;
    private MouseState newState;

    public MouseMovedMessage(int mouseIdx, MouseState newState) {
        this.mouseIdx = mouseIdx;
        this.newState = newState;
    }

    public int getMouseIdx() {
        return mouseIdx;
    }
    public MouseState getNewState() {
        return newState;
    }

    @Override
    protected void alertListeners(Iterable<MessageListener> observers) {
        for (MessageListener observer : observers)
            observer.onMouseMoved(mouseIdx, newState);
    }
}
