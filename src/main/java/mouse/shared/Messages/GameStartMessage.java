package mouse.shared.Messages;

import mouse.shared.Level;

import java.io.Serializable;

/**
 * Created by Florian on 2014-04-12.
 */
public class GameStartMessage extends Message implements Serializable {
    private Level level;

    public GameStartMessage(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    protected void alertObservers(Iterable<MessageObserver> observers) {
        for (MessageObserver observer : observers)
            observer.onGameStart(level);
    }
}
