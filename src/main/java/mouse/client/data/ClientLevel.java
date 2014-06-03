package mouse.client.data;

import mouse.client.network.ServerConnectionListener;
import mouse.server.simulation.Orientation;
import mouse.shared.Level;
import mouse.shared.MouseState;
import mouse.shared.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Florian on 2014-04-12.
 */
public class ClientLevel implements Level, ServerConnectionListener {
    Tile[][] tiles;
    Point baitPosition;
    Collection<Point> startPositions;
    ArrayList<MouseState> mice;

    public ClientLevel(){
        startPositions = new ArrayList<Point>();
        startPositions.add(new Point(2, 2));
        startPositions.add(new Point(4, 4));
        startPositions.add(new Point(6, 6));
        startPositions.add(new Point(8, 8));

        tiles = new Tile[20][20];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = Tile.EMPTY;
            }
        }
        for (int i = 0; i < 20; i++) {
            tiles[0][i] = Tile.WALL;
            tiles[i][0] = Tile.WALL;
            tiles[19][i] = Tile.WALL;
            tiles[i][19] = Tile.WALL;
        }

        tiles[4][4] = Tile.DOOR_CLOSED;
        tiles[10][4] = Tile.DOOR_OPEN;

        mice = new ArrayList<MouseState>();
        mice.add(new MouseState(new Point(7, 1), Orientation.EAST));
        mice.add(new MouseState(new Point(7, 2), Orientation.NORTH));
        mice.add(new MouseState(new Point(7, 3), Orientation.WEST));
        mice.add(new MouseState(new Point(7, 4), Orientation.SOUTH));

        baitPosition = new Point(5, 5);
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
        return mice;
    }



    @Override
    public void onMouseMoved(int mouseIdx, MouseState newState) {
        mice.set(mouseIdx, newState);
    }
    @Override
    public void onDoorStateChanged(Point doorPosition, boolean isClosed) {
        tiles[doorPosition.x][doorPosition.y] = isClosed ? Tile.DOOR_CLOSED
                                                         : Tile.DOOR_OPEN;
    }
    @Override
    public void onGameOver() {
        // handled by MouseJFrame
    }
    
    @Override
    public void onGameStart(Tile[][] tiles, Point baitPosition, Collection<Point> startPositions, Collection<MouseState> mice) {
        this.tiles = tiles;
        this.baitPosition = baitPosition;
        this.startPositions = startPositions;
        this.mice.clear();
        this.mice.addAll(mice);
    }
}