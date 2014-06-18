package mouse.server.network.event;

import mouse.server.simulation.SimulationLevel;

/**
 * An event which should be triggered whenever the level has changed. Listeners
 * can react to this information by sending the new level information to clients
 * or changing the game logic
 *
 * @author kevin_000
 */
public class LevelChangedEvent {

    //Level contains both information of LevelStructure as well as list of mice
    private final SimulationLevel level;

    public LevelChangedEvent(SimulationLevel level) {
        this.level = level;
    }

    /**
     * @return the level
     */
    public SimulationLevel getLevel() {
        return level;
    }

}
