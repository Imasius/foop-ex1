package mouse.server.simulation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class OrientationHelper {
	
	private static OrientationHelper instance = new OrientationHelper();
	
	public static OrientationHelper getInstance() {
		return instance;
	}

	/**
	 * Gives a List that contains all cardinal directions, that lead from start to dest
	 * @param start
	 * @param dest
	 * @return List ordered by suitability of direction
	 */
	public List<Orientation> getViableOrientations(Point start, Point dest) {
		int xDiff = dest.x - start.x;
		int yDiff = dest.y - start.y;
		
		ArrayList<Orientation> result = new ArrayList<Orientation>();
		
		if(isEastOrWestPriorized(xDiff, yDiff)) {
			addEastOrWest(xDiff, result);
			addNorthOrSouth(yDiff, result);
		} else {
			addNorthOrSouth(yDiff, result);
			addEastOrWest(xDiff, result);
		}
			
		return result;
	}
	
	private void addNorthOrSouth(int yDiff, List<Orientation> result) {
		if(yDiff > 0) {
			result.add(Orientation.SOUTH);
		} else if(yDiff < 0) {
			result.add(Orientation.NORTH);
		}
	}

	private void addEastOrWest(int xDiff, List<Orientation> result) {
		if(xDiff > 0) {
			result.add(Orientation.EAST);
		} else if(xDiff < 0) {
			result.add(Orientation.WEST);
		}
	}

	private boolean isEastOrWestPriorized(int xDiff, int yDiff) {
		return abs(xDiff) > abs(yDiff);
	}
}
