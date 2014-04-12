package mouse.shared.Messages;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by Florian on 2014-04-12.
 */
public class GameOverMessage extends Message implements Serializable {
    public GameOverMessage() {
    }

    @Override
    protected void alertObservers(Iterable<MessageObserver> observers) {
        for (MessageObserver observer : observers)
            observer.onGameOver();
    }
}
