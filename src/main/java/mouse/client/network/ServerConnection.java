package mouse.client.network;

import mouse.shared.messages.MessageListener;
import mouse.shared.messages.MessageParser;
import mouse.shared.messages.TryChangeDoorStateMessage;
import mouse.shared.MouseState;
import mouse.shared.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Florian on 2014-04-12.
 */
public class ServerConnection implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ServerConnection.class);

    private final ServerInfo serverInfo;
    private final List<ServerConnectionListener> listeners;
    private final MessageParser msgParser;
    private Socket socket;


    public ServerConnection(ServerInfo serverInfo){
        this.serverInfo = serverInfo;
        this.msgParser = new MessageParser();
        this.listeners = new ArrayList<ServerConnectionListener>();

        this.msgParser.addListener(new MessageListener() {
            @Override
            public void onMouseMoved(int mouseIdx, MouseState newState) {
                for (ServerConnectionListener listener : listeners)
                    listener.onMouseMoved(mouseIdx, newState);
            }
            @Override
            public void onDoorStateChanged(Point doorPosition, boolean isClosed) {
                for (ServerConnectionListener listener : listeners)
                    listener.onDoorStateChanged(doorPosition, isClosed);
            }
            @Override
            public void onGameOver() {
                for (ServerConnectionListener listener : listeners)
                    listener.onGameOver();
            }
            @Override
            public void onTryChangeDoorState(Point doorPosition, boolean tryClose) {
                log.error("Received illegal message.");
                System.exit(1);
            }
            @Override
            public void onGameStart(Tile[][] tiles, Point baitPosition, Collection<Point> startPositions, Collection<MouseState> mice) {
                for (ServerConnectionListener listener : listeners)
                    listener.onGameStart(tiles, baitPosition, startPositions, mice);
            }
        });
    }

    public void addListener(ServerConnectionListener listener) {
        listeners.add(listener);
    }
    public void removeListener(ServerConnectionListener listener) {
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
            try { socket.close(); } catch (IOException ex) { }
            socket = null;
        }
    }

    public void tryChangeDoorState(Point doorPosition, boolean tryClose){
        try {
            new TryChangeDoorStateMessage(doorPosition, tryClose).writeToStream(socket.getOutputStream());
        } catch (IOException ex) {
            log.error("Unable to send message.", ex);
            System.exit(1);
        }
    }
}