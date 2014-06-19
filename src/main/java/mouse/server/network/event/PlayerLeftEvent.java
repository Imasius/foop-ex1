package mouse.server.network.event;

import mouse.server.network.ClientConnectionHandler;

/**
 *
 * @author kevin_000
 */
public class PlayerLeftEvent {

    private ClientConnectionHandler clientConnectionHandler;

    public PlayerLeftEvent(ClientConnectionHandler clientConnectionHandler) {
        this.clientConnectionHandler = clientConnectionHandler;
    }

    public ClientConnectionHandler getClientConnectionHandler() {
        return clientConnectionHandler;
    }
}
