package mouse.server.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import mouse.server.network.event.DoorsChangedEvent;
import mouse.server.network.event.GameOverEvent;
import mouse.server.network.event.LevelChangedEvent;
import mouse.server.network.event.MiceChangedEvent;
import mouse.server.network.event.NotificationEventListener;
import mouse.server.simulation.event.GameLogicEventListener;
import mouse.shared.Door;
import mouse.shared.Mouse;
import mouse.shared.Orientation;
import mouse.shared.Tile;
import mouse.shared.messages.clientToServer.ClientToServerMessageListener;
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
public class MouseGame implements GameLogicEventListener, ClientToServerMessageListener {

    private static final Logger log = LoggerFactory.getLogger(MouseGame.class);
    private final SimulationLevel level;
    private final ArrayList<SimulationMouse> mice = new ArrayList<SimulationMouse>();
    private final ArrayList<Door> changedDoors = new ArrayList<Door>();

    /*I understand that EventQueueTask is central but it should not be responsible for logic!*/
    private Simulator simulator = null;

    private final Collection<NotificationEventListener> listener = new ArrayList<NotificationEventListener>();

    public MouseGame(SimulationLevel level) {
        this.level = level;
    }

    public void addNotificationEventListener(NotificationEventListener listener) {
        this.listener.add(listener);
    }

    public void addMouse() {
        mice.add(new SimulationMouse(level.getStartPositions().get(mice.size()), Orientation.NORTH, mice.size()));
    }

    /**
     * The EventQueueTask defines the tickspeed and all simulations are handled
     * in its thread.
     */
    public void handleTick() {
        /*TODO: Send all mousepositions as one message not as several this is very inefficient */
        if (simulator != null) {
            //Simulate
            ArrayList<Mouse> changed = simulator.simulate(mice, level);

            for (Mouse m : changed) {
                //Test for win condition
                if (m.getPosition().x == level.getBaitPosition().x && m.getPosition().y == level.getBaitPosition().y) {
                    //Set a new baitPosition
                    boolean found = false;
                    while (!found) {
                        int maxX = level.getTiles().length;
                        int maxY = level.getTiles()[0].length;
                        int x = (int) (Math.random() * (double) maxX);
                        int y = (int) (Math.random() * (double) maxY);
                        if (level.getTiles()[x][y] == Tile.EMPTY) {
                            log.debug("New bait positioned");
                            level.setBaitPosition(x, y);
                            found = true;
                        }
                    }
                    for (NotificationEventListener l : listener) {
                        l.handleGameOverEvent(new GameOverEvent(m.getPlayerIndex()));
                        l.handleLevelChangedEvent(new LevelChangedEvent(level));
                    }
                    return;
                }
            }
            //Notify
            for (NotificationEventListener l : listener) {
                if (changed.size() > 0) {
                    for (Mouse m : changed) {
                        //log.debug("Mouse " + m.getPlayerIndex() + " moved to:" + m.getPosition().x + "," + m.getPosition().y);
                    }
                    l.handleMiceChangedEvent(new MiceChangedEvent(changed));
                }
                if (changedDoors.size() > 0) {
                    l.handleDoorsChangedEvent(new DoorsChangedEvent(changedDoors));
                }
            }

            //Per tick every door may change once, maybe change to X ticks
            changedDoors.clear();

        }
    }

    public void start() {
        simulator = new Simulator();
        ArrayList<Mouse> miceUpdateList = new ArrayList<Mouse>();
        for (SimulationMouse m : mice) {
            //convert to share.Mouse (hide unecessary information)
            miceUpdateList.add(m);
        }
        Iterator<NotificationEventListener> iterator = listener.iterator();
        while (iterator.hasNext()) {
            //Send level and mice positions to every players
            NotificationEventListener l = iterator.next();
            l.handleLevelChangedEvent(new LevelChangedEvent(level));
            l.handleMiceChangedEvent(new MiceChangedEvent(miceUpdateList));
        }
    }

    public void stop() {
        simulator = null;
    }

    public void handleOpenDoor(Door door) {
        log.debug("Door change request:" + door.getPosition().x + "," + door.getPosition().y);
        for (Door d : changedDoors) {
            if (d.getPosition().x == door.getPosition().x && d.getPosition().y == door.getPosition().y) {
                //door was alread changed since last tick
                log.debug("Door can only change once per tick!");
                return;
            }
        }
        log.debug("Client request an:" + (door.isOpen() ? "open" : "closed") + " door" + " old state:" + level.getTiles()[door.getPosition().x][door.getPosition().y]);
        level.openDoor(door.getPosition());
        log.debug("New door state:" + (level.getTiles()[door.getPosition().x][door.getPosition().y] == Tile.DOOR_OPEN ? "open" : "closed"));
        changedDoors.add(door);
    }

    public void handleCloseDoor(Door door) {
        for (Door d : changedDoors) {
            if (d.getPosition().x == door.getPosition().x && d.getPosition().y == door.getPosition().y) {
                //door was alread changed since last tick
                return;
            }
        }
        level.closeDoor(door.getPosition());
        changedDoors.add(door);
    }

    public int numberOfMice() {
        return mice.size();
    }
}
