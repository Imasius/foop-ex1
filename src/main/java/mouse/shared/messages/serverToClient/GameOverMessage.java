package mouse.shared.messages.serverToClient;

/**
 * Created by Florian on 2014-04-12. Adapted to ServerToClient Differentiation
 * by Kevin 20014-06-17
 */
public class GameOverMessage extends ServerToClientMessage {

    private int winner;

    public GameOverMessage(int winner) {
        this.winner = winner;
    }

    @Override
    public void alertListeners(Iterable<? extends ServerToClientMessageListener> observers) {
        for (ServerToClientMessageListener observer : observers) {
            observer.handleGameOver(winner);
        }
    }
}
