package mouse.server.network;

import mouse.server.EventQueue;
import mouse.server.ServerConfiguration;
import mouse.server.level.LevelLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mouse.server.network.event.DoorsChangedEvent;
import mouse.server.network.event.GameOverEvent;
import mouse.server.network.event.LevelChangedEvent;
import mouse.server.network.event.MiceChangedEvent;
import mouse.server.network.event.NotificationEventListener;
import mouse.server.network.event.PlayerJoinedEvent;
import mouse.server.network.event.PlayerLeftEvent;
import mouse.server.simulation.MouseGame;
import mouse.shared.Mouse;
import mouse.shared.messages.serverToClient.GameOverMessage;
import mouse.shared.messages.serverToClient.UpdateDoorsMessage;
import mouse.shared.messages.serverToClient.UpdateLevelMessage;
import mouse.shared.messages.serverToClient.UpdateMiceMessage;

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
        eventQueue.addClientToServerMessageListener(game);
        //EventQueue notifies MouseServer for GameLogicEvents
        //MouseServer decides if he needs to passes events to game
        eventQueue.addGameLogicEventListener(game);
        //If game recieves notifications, it updates everything and possibly
        //fires an event to the MouseServer who propagates everything 
        game.addNotificationEventListener(this);
        
        try {
            serverSocket = new ServerSocket(serverConfiguration.getServerPort());
            clientListener = new ClientListener(serverSocket, eventQueue.getQueue());
            clientListener.registerPlayerJoinedEventListener(this);
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

    //Send Notifications to player when some structure has changed
    public void handleDoorsChangedEvent(DoorsChangedEvent event) {
        //log.debug("Sent :" + event.getDoors().size() + " door updates to:" + clientList.size() + " clients");
        synchronized (clientList) {
            Iterator<ClientConnectionHandler> it = clientList.iterator();
            while (it.hasNext()) {
                it.next().sendMessage(new UpdateDoorsMessage(event.getDoors()));
            }
        }
    }
    
    public void handleLevelChangedEvent(LevelChangedEvent event) {
        log.debug("Send new Level to:" + clientList.size());
        synchronized (clientList) {
            Iterator<ClientConnectionHandler> it = clientList.iterator();
            while (it.hasNext()) {
                it.next().sendMessage(new UpdateLevelMessage(event.getLevel()));
            }
        }
    }
    
    public void handleMiceChangedEvent(MiceChangedEvent event) {
//        log.debug("Sent :" + event.getMice().size() + " mice-updates to:" + clientList.size() + " clients");        
//        for (Mouse m : event.getMice()) {
//            log.debug(">pos:" + m.getPosition().x + "," + m.getPosition().y);
//        }
        synchronized (clientList) {
            Iterator<ClientConnectionHandler> it = clientList.iterator();
            while (it.hasNext()) {
                it.next().sendMessage(new UpdateMiceMessage(event.getMice()));
            }
        }
        
    }

    //GameLogicEvents - Player joins and ticks
    public void handleTick() {
        //Server does only pass ticks
        game.handleTick();
    }
    
    public void handlePlayerJoinedEvent(PlayerJoinedEvent event) {
        if (clientList.size() == serverConfiguration.getPlayerCount()) {
            log.debug("Game is full");
        } else if (clientList.size() < serverConfiguration.getPlayerCount()) {
            log.debug("Player joined the game");
            synchronized (clientList) {
                clientList.add(event.getClientConnectionHandler());
            }
            //Listen for possible player left events
            event.getClientConnectionHandler().registerNotificationEventListener(this);
            //Possible add a new mouse, always for a new game, but for rejoins not necessary
            if (game.numberOfMice() < clientList.size()) {
                game.addMouse();
            }
            
            if (game.numberOfMice() == serverConfiguration.getPlayerCount()) {
                log.debug("Game is full and ready!");
                game.start();
            }
            
        } else {
            log.error("More players in the game than allowed");
        }
    }
    
    public void handleGameOverEvent(GameOverEvent event) {
        log.debug("Sent winner notification: Player " + event.getWinner() + " has won");
        synchronized (clientList) {
            Iterator<ClientConnectionHandler> it = clientList.iterator();
            while (it.hasNext()) {
                it.next().sendMessage(new GameOverMessage(event.getWinner()));
            }
        }
    }
    
    public void handlePlayerLeftEvent(PlayerLeftEvent event) {
        synchronized (clientList) {
            clientList.remove(event.getClientConnectionHandler());
        }
    }
}
