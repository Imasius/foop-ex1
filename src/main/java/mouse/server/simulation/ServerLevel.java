package mouse.server.simulation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import mouse.shared.Level;
import mouse.shared.MouseState;
import mouse.shared.Tile;

public class ServerLevel implements Level {
    Tile[][] tiles;
    Point baitPosition;
    Collection<Point> startPositions;
    ArrayList<Mouse> mice;


    @Override
    public int getHeight() {
        return tiles[0].length;
    }
    @Override
    public int getWidth() {
        return tiles.length;
    }

	@Override
	public Tile tileAt(int x, int y) {
		return tiles[x][y];
	}
	
    @Override
    public Point getBaitPosition() {
        return baitPosition;
    }
    @Override
    public Collection<Point> getStartPositions() {
        return startPositions;
    }
    @Override
    public Collection<MouseState> getMice() {
    	ArrayList<MouseState> states = new ArrayList<MouseState>();
    	
    	for(Mouse m : mice) {
    		states.add(new MouseState(m.getPosition(), m.getLastOrientation()));
    	}
        return states;
    }
}
