package mouse.server.simulation;

import java.awt.Point;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a Mouse in the simulation component.
 * User: Markus
 * Date: 02.04.14
 */
public class Mouse {
	private Point position;
	private MouseState state;
	private LevelAdapter levelAdapter;
	private int randomCounter;
	private Orientation randomDirection;
	private Orientation lastOrientation = Orientation.NORTH;
	
	private static final Logger log = LoggerFactory.getLogger(Mouse.class);
	
	public Mouse(Point position, LevelAdapter level) {
		log.debug("Mouse created at {}.", position);
		
		state = MouseState.MOVING_DIRECTED;
		this.position = position;
		this.levelAdapter = level;
	}
	
	public Orientation move() {
		Point bait = levelAdapter.getLevel().getBaitPosition();
		Orientation ret = null;
		switch (state) {
		case MOVING_DIRECTED:
			for(Orientation o : OrientationHelper.getInstance().getViableOrientations(position, bait)) {
				if(levelAdapter.isDirectionFeasible(position, o)) {
					ret = o;
					break;
				}
			}
			
			confuse();
			ret = randomDirection;
			break;
		case MOVING_RANDOM:
			if(!levelAdapter.isDirectionFeasible(position, randomDirection)) {
				randomDirection = levelAdapter.getRandomFeasibleDirection(position);
			}
			ret = randomDirection;
			randomCounter--;
			
			if(randomCounter <= 0) {
				state = MouseState.MOVING_DIRECTED;
			}
			break;
		}
		
		lastOrientation = ret;
		return ret;
	}
	
	public void applyMotion(Orientation direction) {
		position = OrientationHelper.getInstance().applyOrientation(position, direction);
	}
	
	public Point getPosition() {
		return position;
	}

	public void confuse() {
		state = MouseState.MOVING_RANDOM;
		randomDirection = levelAdapter.getRandomFeasibleDirection(position);
		randomCounter = 4;
	}

	public Orientation getLastOrientation() {
		return lastOrientation;
	}
	
}
