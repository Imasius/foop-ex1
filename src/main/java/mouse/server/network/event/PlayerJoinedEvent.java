/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mouse.server.network.event;

import mouse.server.network.ClientConnectionHandler;

/**
 *
 * @author Kevin Streicher <e1025890@student.tuwien.ac.at>
 */
public class PlayerJoinedEvent {

    private final ClientConnectionHandler clientConnectionHandler;

    public PlayerJoinedEvent(ClientConnectionHandler clientConnectionHandler) {
        this.clientConnectionHandler = clientConnectionHandler;
    }

    public ClientConnectionHandler getClientConnectionHandler() {
        return clientConnectionHandler;
    }
}
