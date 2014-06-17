package mouse.shared.messages.clientToServer;

import mouse.shared.Door;

/**
 *
 * @author kevin_000
 */
public class RequestDoorStateMessage extends ClientToServerMessage {

    private final Door door;

    public RequestDoorStateMessage(Door door) {
        this.door = door;
    }

    public Door getDoor() {
        return door;
    }

    @Override
    public void alertListeners(Iterable<? extends ClientToServerMessageListener> observers) {
        for (ClientToServerMessageListener observer : observers) {
            if (door.isClosed()) {
                observer.handleCloseDoor(door);
            } else if (door.isOpen()) {
                observer.handleOpenDoor(door);
            }
        }
    }
}
