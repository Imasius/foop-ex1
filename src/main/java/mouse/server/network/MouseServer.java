package mouse.server.network;

import java.awt.Point;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import mouse.server.EventQueue;
import mouse.server.ServerConfiguration;
import mouse.server.event.CloseDoorEvent;
import mouse.server.event.GameLogicEventListener;
import mouse.server.event.OpenDoorEvent;
import mouse.server.event.PlayerJoinedEvent;
import mouse.server.simulation.LevelAdapter;
import mouse.server.simulation.Mouse;
import mouse.server.simulation.ServerLevel;
import mouse.server.simulation.Simulator;
import mouse.shared.messages.DoorStateChangedMessage;
import mouse.shared.messages.GameStartMessage;
import mouse.shared.messages.MouseMovedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The server for our mouse game. Use this class inside the client to create a
 * server without running a separate process. See StandaloneServer for a example
 * on usage.
 *
 * User: Simon Date: 21.03.14
 */
public class MouseServer implements GameLogicEventListener {

    private static final Logger log = LoggerFactory.getLogger(MouseServer.class);

    private ServerSocket serverSocket;
    private final Broadcaster broadcaster;
    private final ServerConfiguration serverConfiguration;
    private final EventQueue eventQueue;
    private final ServerLevel level;/*Not final ?- allow loading a new level?*/

    private Simulator simulator;/*I understand that EventQueueTask is central but it should not be responsible for logic!*/

    private ClientListener clientListener;
    private final List<ClientConnectionHandler> clientList;

    public MouseServer() {
        serverConfiguration = ServerConfiguration.load();
        broadcaster = new Broadcaster(serverConfiguration);
        eventQueue = new EventQueue(serverConfiguration.getTickInterval());
        level = new ServerLevel(serverConfiguration.getLevelID());
        clientList = new ArrayList<ClientConnectionHandler>();
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

    public void handleOpenDoorEvent(OpenDoorEvent event) {
        /*TODO: Send all door changes as one message not as several this is very inefficient */
        /*Change the Level*/
        level.openDoor(event.doorPosition);
        /*Notify players*/
        Iterator<ClientConnectionHandler> it = clientList.iterator();
        DoorStateChangedMessage m = new DoorStateChangedMessage(event.doorPosition, false);
        while (it.hasNext()) {
            it.next().sendMessage(m);
        }
    }

    public void handleCloseDoorEvent(CloseDoorEvent event) {
        /*TODO: Send all door changes as one message not as several this is very inefficient */
        /*Change the Level*/
        level.closeDoor(event.doorPosition);
        /*Notify players*/
        Iterator<ClientConnectionHandler> it = clientList.iterator();
        DoorStateChangedMessage m = new DoorStateChangedMessage(event.doorPosition, true);
        while (it.hasNext()) {
            it.next().sendMessage(m);
        }
    }

    /**
     * The EventQueueTask defines the tickspeed and all simulations are handled
     * in its thread.
     */
    public void handleTick() {
        /*TODO: Send all mousepositions as one message not as several this is very inefficient */
        if (simulator != null) {
            List<mouse.shared.MouseState> newStates = simulator.simulate();
            for (int idx = 0; idx < newStates.size(); idx++) {
                MouseMovedMessage m = new MouseMovedMessage(idx, newStates.get(idx));
                Iterator<ClientConnectionHandler> it = clientList.iterator();
                while (it.hasNext()) {
                    it.next().sendMessage(m);
                }
            }
            log.debug("Updated:"+newStates.size()+" mice");
        }
    }

    /**
     * Chek if this server can handle more players, if the preset number of
     * players is reached start a game
     *
     * @param event
     */
    public void handlePlayerJoinedEvent(PlayerJoinedEvent event) {
        if (level.getMice().size() >= serverConfiguration.getPlayerCount()) {
            log.info("Game is full. Player can not join!");
            return;
        }
        clientList.add(event.clientConnectionHandler);
        log.info("Player joined the game.");
        Collection<Point> startPositions = level.getStartPositions();
        Point startPosition = null;
        Iterator<Point> it = startPositions.iterator();
        while (it.hasNext()) {
            startPosition = it.next();
        }
        level.addMouse(new Mouse(startPosition, new LevelAdapter(level)));
        if (level.getMice().size() == serverConfiguration.getPlayerCount()) {
            log.info("Sufficient Players - Game started!");
            simulator = new Simulator(level.getMiceList());
            GameStartMessage m = ServerLevel.toGameStartMessage(level);
            Iterator<ClientConnectionHandler> itc = clientList.iterator();
            while (itc.hasNext()) {
                itc.next().sendMessage(m);
            }
        }
    }
}
