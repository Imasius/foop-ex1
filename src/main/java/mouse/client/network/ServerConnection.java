package mouse.client.network;

import mouse.shared.Level;
import mouse.shared.Messages.MessageObserver;
import mouse.shared.Messages.MessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian on 2014-04-12.
 */
public class ServerConnection implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ServerConnection.class);

    private final ServerInfo serverInfo;
    private final List<ServerConnectionObserver> observers;
    private final MessageParser msgParser;


    public ServerConnection(ServerInfo serverInfo){
        this.serverInfo = serverInfo;
        this.msgParser = new MessageParser();
        this.observers = new ArrayList<ServerConnectionObserver>();

        this.msgParser.addObserver(new MessageObserver() {
            @Override
            public void onMouseMoved(int mouseIdx, Point newPosition) {
                for (ServerConnectionObserver observer : observers)
                    observer.onMouseMoved(mouseIdx, newPosition);
            }
            @Override
            public void onDoorStateChanged(Point doorPosition, boolean isClosed) {
                for (ServerConnectionObserver observer : observers)
                    observer.onDoorStateChanged(doorPosition, isClosed);
            }
            @Override
            public void onGameOver() {
                for (ServerConnectionObserver observer : observers)
                    observer.onGameOver();
            }
            @Override
            public void onGameStart(Level level) {
                for (ServerConnectionObserver observer : observers)
                    observer.onGameStart(level);
            }
        });
    }

    public void addObserver(ServerConnectionObserver observer) {
        observers.add(observer);
    }
    public void removeObserver(ServerConnectionObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void run() {
        Socket socket = null;

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
        }
    }
}
