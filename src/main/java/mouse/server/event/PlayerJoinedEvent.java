package mouse.server.event;

import java.net.Socket;
import mouse.server.network.ClientConnectionHandler;

/**
 *
 * @author Kevin Streicher <e1025890@student.tuwien.ac.at>
 */
public class PlayerJoinedEvent {

    public ClientConnectionHandler clientConnectionHandler;

    public PlayerJoinedEvent(ClientConnectionHandler handler) {
        this.clientConnectionHandler = handler;
    }

}
