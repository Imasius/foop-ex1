package mouse.shared.messages;

import java.io.Serializable;

/**
 * Created by Florian on 2014-04-12.
 */
public class GameOverMessage extends Message implements Serializable {
    public GameOverMessage() {
    }

    @Override
    protected void alertListeners(Iterable<MessageListener> observers) {
        for (MessageListener observer : observers)
            observer.onGameOver();
    }
}
