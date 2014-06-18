package mouse.client.network;

import mouse.shared.messages.clientToServer.RequestDoorStateMessage;
import mouse.shared.Mouse;
import mouse.shared.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import mouse.server.simulation.SimulationMouse;
import mouse.shared.Door;
import mouse.shared.messages.clientToServer.ClientToServerMessage;
import mouse.shared.messages.serverToClient.ServerToClientMessageListener;

/**
 * Created by Florian on 2014-04-12.
 */
public class ServerConnection implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ServerConnection.class);

    private final ServerInfo serverInfo;
    private final List<ServerToClientMessageListener> listeners;
    private final ClientMessageParser msgParser;
    private Socket socket;

    public ServerConnection(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
        this.msgParser = new ClientMessageParser();
        this.listeners = new ArrayList<ServerToClientMessageListener>();

        this.msgParser.addListener(new ServerToClientMessageListener() {

            public void handleUpdateMice(ArrayList<Mouse> mice) {
                for (ServerToClientMessageListener listener : listeners) {
                    listener.handleUpdateMice(mice);
                }
            }

            public void handleUpdateDoors(ArrayList<Door> doors) {
                for (ServerToClientMessageListener listener : listeners) {
                    listener.handleUpdateDoors(doors);
                }
            }

            public void handleGameStart(Tile[][] tiles, Point baitPosition, Collection<Point> startPositions, Collection<Mouse> mice) {
                for (ServerToClientMessageListener listener : listeners) {
                    listener.handleGameStart(tiles, baitPosition, startPositions, mice);
                }
            }

            public void handleGameOver() {
                for (ServerToClientMessageListener listener : listeners) {
                    listener.handleGameOver();
                }
            }
        });
    }

    public void addListener(ServerToClientMessageListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ServerToClientMessageListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void run() {
        try {
            socket = new Socket(serverInfo.getAddress(), 30330);
        } catch (IOException ex) {
            log.error("Unable to create connection socket.", ex);
            System.exit(1);
        }

        try {
            while (true) {
                msgParser.parseMessage(socket.getInputStream());
            }
        } catch (IOException e) {
            log.debug("Shutting down connection.");
            try {
                socket.close();
            } catch (IOException ex) {
            }
            socket = null;
        }
    }

    public void requestDoorState(Point doorPosition, boolean closed) {
        sendMessage(new RequestDoorStateMessage(new Door(doorPosition, closed)));
    }

    private void sendMessage(ClientToServerMessage m) {
        try {
            m.writeToStream(socket.getOutputStream());
        } catch (IOException ex) {
            log.error("Unable to send message.", ex);
            System.exit(1);
        }
    }
}
