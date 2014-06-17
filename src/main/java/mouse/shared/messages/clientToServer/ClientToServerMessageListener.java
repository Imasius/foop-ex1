package mouse.shared.messages.clientToServer;

import mouse.shared.Door;

/**
 * The ClientToServerMessageListener listens for messages from client to server
 *
 * @author kevin_000
 */
public interface ClientToServerMessageListener {

    void handleOpenDoor(Door door);

    void handleCloseDoor(Door door);
}
