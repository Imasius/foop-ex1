package mouse.server.simulation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mouse.shared.Level;
import mouse.shared.MouseState;
import mouse.shared.Tile;
import mouse.shared.messages.GameStartMessage;

public class ServerLevel implements Level {
	private Tile[][] tiles;
	private Point baitPosition = new Point();
	private Collection<Point> startPositions = new ArrayList<Point>();
	private List<Mouse> mice = new ArrayList<Mouse>();

	public ServerLevel(int levelId) {
		if (levelId == 1) {
			tiles = new Tile[50][50];

			for (int i = 0; i < 50; ++i) {
				for (int j = 0; j < 50; ++j) {
					if (i == 0 || j == 0 || j == 25 || i == 25) {
						tiles[i][j] = Tile.WALL;
					} else {
						tiles[i][j] = Tile.EMPTY;
					}
				}
			}

			tiles[25][38] = Tile.DOOR_CLOSED;
			tiles[25][13] = Tile.DOOR_CLOSED;
			tiles[38][25] = Tile.DOOR_CLOSED;
			tiles[13][25] = Tile.DOOR_CLOSED;
			
			baitPosition = new Point(30, 30);
			
			startPositions.add(new Point(3,3));
			startPositions.add(new Point(46,3));
			startPositions.add(new Point(3,46));
			startPositions.add(new Point(46,46));
		}
	}

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

		for (Mouse m : mice) {
			states.add(new MouseState(m.getPosition(), m.getLastOrientation()));
		}
		return states;
	}

	public void addMouse(Mouse mouse) {
		mice.add(mouse);
	}

	public static GameStartMessage toGameStartMessage(ServerLevel level) {
		return new GameStartMessage(level.tiles, level.baitPosition,
				level.startPositions, level.getMice());
	}
}
