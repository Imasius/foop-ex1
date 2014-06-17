package mouse.server.network.event;

/**
 * Notification events are events which needs to be send from game logic to all
 * players
 *
 * @author kevin_000
 */
public interface NotificationEventListener {

    /*Notification about */
    public void handleDoorsChangedEvent(DoorsChangedEvent event);

    public void handleLevelChangedEvent(LevelChangedEvent event);

    public void handleMiceChangedEvent(MiceChangedEvent event);
}
