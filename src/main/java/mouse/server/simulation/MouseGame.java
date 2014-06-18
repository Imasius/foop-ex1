package mouse.server.simulation;

import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import mouse.server.simulation.event.CloseDoorEvent;
import mouse.server.simulation.event.GameLogicEventListener;
import mouse.server.simulation.event.OpenDoorEvent;
import mouse.server.simulation.event.PlayerJoinedEvent;
import mouse.server.network.ClientConnectionHandler;
import mouse.shared.messages.serverToClient.GameStartMessage;
import mouse.shared.messages.serverToClient.UpdateDoorsMessage;
import mouse.shared.messages.serverToClient.UpdateMiceMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SimulationMouseGame handles all information of a game and is the eventhandler
 * for GameLogicEvents. This class separates SimulationMouseServer which is
 * responsible for the network layer and Simulation which is responsible for the
 * Game Logic itself
 *
 * @author kevin_000
 */
public class MouseGame implements GameLogicEventListener {

    private static final Logger log = LoggerFactory.getLogger(MouseGame.class);
    private final SimulationLevel level;
    private Simulator simulator;/*I understand that EventQueueTask is central but it should not be responsible for logic!*/


    public MouseGame(SimulationLevel level) {
        this.level = level;
    }

    public void handleOpenDoorEvent(OpenDoorEvent event) {
        /*TODO: Send all door changes as one message not as several this is very inefficient */
        /*Change the SimulationLevel*/
        level.openDoor(event.doorPosition);
        /*Notify players*/
        Iterator<ClientConnectionHandler> it = clientList.iterator();
        UpdateDoorsMessage m = new UpdateDoorsMessage(event.doorPosition, false);
        while (it.hasNext()) {
            it.next().sendMessage(m);
        }
    }

    public void handleCloseDoorEvent(CloseDoorEvent event) {
        /*TODO: Send all door changes as one message not as several this is very inefficient */
        /*Change the SimulationLevel*/
        level.closeDoor(event.doorPosition);
        /*Notify players*/
        Iterator<ClientConnectionHandler> it = clientList.iterator();
        UpdateDoorsMessage m = new UpdateDoorsMessage(event.doorPosition, true);
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
            List<mouse.shared.Mouse> newStates = simulator.simulate();
            for (int idx = 0; idx < newStates.size(); idx++) {
                UpdateMiceMessage m = new UpdateMiceMessage(idx, newStates.get(idx));
                Iterator<ClientConnectionHandler> it = clientList.iterator();
                while (it.hasNext()) {
                    it.next().sendMessage(m);
                }
            }
            log.debug("Updated:" + newStates.size() + " mice");
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
        level.addMouse(new SimulationMouse(startPosition, new LevelAdapter(level), level.getMice().size()));
        if (level.getMice().size() == serverConfiguration.getPlayerCount()) {
            log.info("Sufficient Players - Game started!");
            simulator = new Simulator(level.getMice());
            GameStartMessage m = level.toGameStartMessage();
            Iterator<ClientConnectionHandler> itc = clientList.iterator();
            while (itc.hasNext()) {
                itc.next().sendMessage(m);
            }
        }
    }
}
