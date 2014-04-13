package mouse.shared.Messages;

import mouse.shared.MouseState;
import mouse.shared.Tile;

import java.awt.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Florian on 2014-04-12.
 */
public class GameStartMessage extends Message implements Serializable {
    private Tile[][] tiles;
    private Point baitPosition;
    private Collection<Point> startPositions;
    private Collection<MouseState> mice;

    public GameStartMessage(Tile[][] tiles, Point baitPosition, Collection<Point> startPositions, Collection<MouseState> mice) {
        this.tiles = tiles;
        this.baitPosition = baitPosition;
        this.startPositions = startPositions;
        this.mice = mice;
    }

    public Collection<MouseState> getMice() {
        return mice;
    }
    public Point getBaitPosition() {
        return baitPosition;
    }
    public Collection<Point> getStartPositions() {
        return startPositions;
    }
    public Tile[][] getTiles() {
        return tiles;
    }

    @Override
    protected void alertListeners(Iterable<MessageListener> observers) {
        for (MessageListener observer : observers)
            observer.onGameStart(tiles, baitPosition, startPositions, mice);
    }
}
