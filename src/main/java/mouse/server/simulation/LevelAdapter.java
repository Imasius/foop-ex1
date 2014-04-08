package mouse.server.simulation;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import mouse.shared.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides some convenience methods for Level
 * User: Markus
 * Date: 08.04.14
 */
public class LevelAdapter {
	private static final Logger log = LoggerFactory.getLogger(LevelAdapter.class);
	
	private Level level;

	public LevelAdapter(Level level) {
		log.debug("LevelAdapter created.");
		this.level = level;
	}
	
	public Level getLevel() {
		return level;
	}
	
	private Point applyOrientation(Point position, Orientation direction) {
		switch (direction) {
		case NORTH:
			 return new Point(position.x, position.y -1);
		case SOUTH:
			return new Point(position.x, position.y +1);
		case EAST:
			return new Point(position.x +1, position.y);
		case WEST:
			return new Point(position.x -1, position.y);
		}
		throw new IllegalArgumentException("Unsupported direction!");
	}

	public boolean isDirectionFeasible(Point position, Orientation direction) {
		Point checkedPosition = applyOrientation(position, direction);
		
		if(checkedPosition.x < 0 || checkedPosition.y < 0 || 
				checkedPosition.x >= level.getWidth() || checkedPosition.y >= level.getHeight()) {
			return false;
		}
		
		switch (level.tileAt(checkedPosition.x, checkedPosition.y)) {
		case DOOR_CLOSED:
		case WALL:
			return false;
		case DOOR_OPEN:
		case EMPTY:
			return true;
		}
		throw new IllegalArgumentException("Unsupported TileType!");
	}
	
	public Orientation getRandomFeasibleDirection(Point position) {
		List<Orientation> list = Arrays.asList(Orientation.values());
		Collections.shuffle(list);
		
		for(Orientation direction : list) {
			if(this.isDirectionFeasible(position, direction)) {
				return direction;
			}
		}
		
		throw new RuntimeException("Illegal Gamestate! Mouse wants to move but cannot!");
	}
}
