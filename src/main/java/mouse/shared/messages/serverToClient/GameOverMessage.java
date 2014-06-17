package mouse.shared.messages.serverToClient;

import java.io.Serializable;

/**
 * Created by Florian on 2014-04-12. Adapted to ServerToClient Differentiation
 * by Kevin 20014-06-17
 */
public class GameOverMessage extends ServerToClientMessage implements Serializable {

    public GameOverMessage() {
    }

    @Override
    public void alertListeners(Iterable<? extends ServerToClientMessageListener> observers) {
        for (ServerToClientMessageListener observer : observers) {
            observer.handleGameOver();
        }
    }
}
