package mouse.server.simulation.event;

/**
 * The GameLogicEventListener hierachy encapsulates all events not handled by
 * the server and provides an interface between the network layer and the logic
 * layer
 *
 * @author Kevin Streicher <e1025890@student.tuwien.ac.at>
 */
public interface GameLogicEventListener {

    /*Notify the Game Logic about one timer tick, such that it can simulate the next step*/
    public void handleTick();


}
