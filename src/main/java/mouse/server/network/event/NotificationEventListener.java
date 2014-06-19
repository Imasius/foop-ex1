package mouse.server.network.event;

/**
 * Notification events are events which needs to be send from server to all
 * players (=Notification)
 *
 * @author kevin_000
 */
public interface NotificationEventListener {

    /*Notification about */
    public void handleDoorsChangedEvent(DoorsChangedEvent event);

    public void handleLevelChangedEvent(LevelChangedEvent event);

    public void handleMiceChangedEvent(MiceChangedEvent event);

    public void handleGameOverEvent(GameOverEvent event);

    /*Notify the Game Logic that a player has joined*/
    public void handlePlayerJoinedEvent(PlayerJoinedEvent event);

    /*Notify the Server a client has left*/
    public void handlePlayerLeftEvent(PlayerLeftEvent event);
}
