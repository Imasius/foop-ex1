package mouse.server.network;

import mouse.server.EventQueue;
import mouse.server.ServerConfiguration;
import mouse.server.simulation.event.CloseDoorEvent;
import mouse.server.simulation.event.GameLogicEventListener;
import mouse.server.simulation.event.OpenDoorEvent;
import mouse.server.simulation.event.PlayerJoinedEvent;
import mouse.server.level.LevelLoader;
import mouse.server.simulation.LevelAdapter;
import mouse.server.simulation.Mouse;
import mouse.server.simulation.Simulator;
import mouse.shared.Level;
import mouse.shared.messages.UpdateDoorsMessage;
import mouse.shared.messages.serverToClient.GameStartMessage;
import mouse.shared.messages.UpdateMiceMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import mouse.server.network.event.DoorsChangedEvent;
import mouse.server.network.event.LevelChangedEvent;
import mouse.server.network.event.MiceChangedEvent;
import mouse.server.network.event.NotificationEventListener;
import mouse.server.simulation.MouseGame;

/**
 * The server for our mouse game. Use this class inside the client to create a
 * server without running a separate process. See StandaloneServer for a example
 * on usage.
 *
 * User: Simon Date: 21.03.14
 */
public class MouseServer implements NotificationEventListener {

    private static final Logger log = LoggerFactory.getLogger(MouseServer.class);

    private ServerSocket serverSocket;
    private final Broadcaster broadcaster;
    private final ServerConfiguration serverConfiguration;
    private final EventQueue eventQueue;

    private ClientListener clientListener;
    private final List<ClientConnectionHandler> clientList;

    private MouseGame game;

    public MouseServer() {
        serverConfiguration = ServerConfiguration.load();
        broadcaster = new Broadcaster(serverConfiguration);
        eventQueue = new EventQueue(serverConfiguration.getTickInterval());
        clientList = new ArrayList<ClientConnectionHandler>();

        //The game running on a server
        game = new MouseGame(LevelLoader.loadLevel(serverConfiguration.getLevel()));
    }

    /**
     * @return True if the server startup was successful.
     */
    public boolean start() {
        log.info("Starting mouse game server.");
        eventQueue.registerGameLogicEventListener(this);

        try {
            serverSocket = new ServerSocket(serverConfiguration.getServerPort());
            clientListener = new ClientListener(serverSocket, eventQueue.getQueue());
            clientListener.registerGameLogicEventListener(this);
            log.debug("Server is listening on port {}", serverConfiguration.getServerPort());
        } catch (IOException e) {
            log.error("Unable to create server socket.", e);
            return false;
        }
        clientListener.start();

        if (serverConfiguration.isBroadcastEnabled()) {
            broadcaster.start();
        }

        eventQueue.start();
        return true;
    }

    public void stop() {
        log.info("Shutting down server.");
        try {
            serverSocket.close();
        } catch (IOException ex) {
            log.warn("Exception while closing the ServerSocket.", ex);
        }

        if (serverConfiguration.isBroadcastEnabled()) {
            broadcaster.stop();
        }
        eventQueue.stop();
    }

    public void handleDoorsChangedEvent(DoorsChangedEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleLevelChangedEvent(LevelChangedEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleMiceChangedEvent(MiceChangedEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
