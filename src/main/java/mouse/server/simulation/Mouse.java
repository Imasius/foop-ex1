package mouse.server.simulation;

import java.awt.Point;

import mouse.shared.Level;

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
	private Level level;
	
	private static final Logger log = LoggerFactory.getLogger(Mouse.class);
	
	public Mouse(Point position, Level level) {
		log.debug("Mouse created at {}.", position);
		
		state = MouseState.MOVING_DIRECTED;
		this.position = position;
		this.level = level;
	}
	
	public Orientation move() {
		Point bait = level.getBaitPosition();
		Orientation ret = null;
		switch (state) {
		case MOVING_DIRECTED:
			for(Orientation o : OrientationHelper.getInstance().getViableOrientations(position, bait)) {
				//TODO check for collision with Level
				ret = o;
				break;
			}
			state = MouseState.MOVING_RANDOM;
			//this fall-through is intended
		case MOVING_RANDOM:
			//TODO select suitable random orientation
			break;
		}
		
		return ret;
	}
}
