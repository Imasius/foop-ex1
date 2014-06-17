package mouse.shared.messages.serverToClient;

import mouse.shared.Mouse;
import mouse.shared.Tile;

import java.awt.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Florian on 2014-04-12. Adapted to ServerToClient Differentiation
 * by Kevin 20014-06-17
 */
public class GameStartMessage extends ServerToClientMessage implements Serializable {

    private final Tile[][] tiles;
    private final Point baitPosition;
    private final Collection<Point> startPositions;
    private final Collection<Mouse> mice;

    public GameStartMessage(Tile[][] tiles, Point baitPosition, Collection<Point> startPositions, Collection<Mouse> mice) {
        this.tiles = tiles;
        this.baitPosition = baitPosition;
        this.startPositions = startPositions;
        this.mice = mice;
    }

    public Collection<Mouse> getMice() {
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
    public void alertListeners(Iterable<? extends ServerToClientMessageListener> observers) {
        for (ServerToClientMessageListener observer : observers) {
            observer.handleGameStart(tiles, baitPosition, startPositions, mice);
        }
    }
}
