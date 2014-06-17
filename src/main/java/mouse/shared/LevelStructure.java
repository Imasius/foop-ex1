package mouse.shared;

import mouse.shared.messages.serverToClient.GameStartMessage;

import java.awt.*;
import java.util.Collection;

/**
 * Abstract Level to be used on both Server and Client. Coordinates start at
 * (0,0) in the upper-left corner and range to (width-1, height-1). User: Markus
 * Date: 02.04.14
 *
 * Added Collection<Pair<Point, Orientation>> getMousePosition() user: Kevin Date: 06.04.14
 */
public interface LevelStructure {

    int getHeight();

    int getWidth();

    Tile tileAt(int x, int y);

    void setTileAt(int x, int y, Tile tile);

    Point getBaitPosition();

    Collection<Point> getStartPositions();

    GameStartMessage toGameStartMessage();
}
