package mouse.server.simulation;

import java.awt.*;
import java.util.ArrayList;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import mouse.shared.LevelStructure;
import mouse.shared.Orientation;
import mouse.shared.Tile;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 *
 * @author Markus Scherer, 8.4 adapted to new Level Test
 */
public class SimulationLevelTest {

    private SimulationLevel level;
    private Point position = null;
    private Tile[][] tiles;

    @Before
    public void setUp() throws Exception {
        position = new Point(1, 1);

        tiles = new Tile[2][2];
        tiles[0][0] = Tile.EMPTY;
        tiles[0][1] = Tile.DOOR_OPEN;
        tiles[1][0] = Tile.WALL;
        tiles[1][1] = Tile.DOOR_CLOSED;
        ArrayList<Point> startPositions = new ArrayList<Point>();
        startPositions.add(position);
        level = spy(new SimulationLevel(new LevelStructure(tiles, position, startPositions) {

        }));

        when(level.getHeight()).thenReturn(3);
        when(level.getWidth()).thenReturn(3);
    }

    private void testHelper(int xDiff, int yDiff, Tile tileType, Orientation direction, boolean expected) {
        Point examine = position;
        examine.x += xDiff;
        examine.y += yDiff;

        when(level.tileAt(examine.x, examine.y)).thenReturn(tileType);
        boolean result = level.isDirectionFeasible(position, direction);

        if (expected) {
            assertTrue(tileType + " should be feasible!", result);
        } else {
            assertFalse(tileType + " should not be feasible!", result);
        }
    }

    @Test
    public void wallsNorthAreNotFeasible() {
        testHelper(0, -1, Tile.WALL, Orientation.NORTH, false);
    }

    @Test
    public void wallsSouthAreNotFeasible() {
        testHelper(0, 1, Tile.WALL, Orientation.SOUTH, false);
    }

    @Test
    public void wallsEastAreNotFeasible() {
        testHelper(1, 0, Tile.WALL, Orientation.EAST, false);
    }

    @Test
    public void wallsDoorsWestAreNotFeasible() {
        testHelper(-1, 0, Tile.WALL, Orientation.WEST, false);
    }

    @Test
    public void closedDoorsNorthAreNotFeasible() {
        testHelper(0, -1, Tile.DOOR_CLOSED, Orientation.NORTH, false);
    }

    @Test
    public void closedDoorsSouthAreNotFeasible() {
        testHelper(0, 1, Tile.DOOR_CLOSED, Orientation.SOUTH, false);
    }

    @Test
    public void closedDoorsEastAreNotFeasible() {
        testHelper(1, 0, Tile.DOOR_CLOSED, Orientation.EAST, false);
    }

    @Test
    public void closedDoorsWestAreNotFeasible() {
        testHelper(-1, 0, Tile.DOOR_CLOSED, Orientation.WEST, false);
    }

    @Test
    public void openDoorsNorthAreFeasible() {
        testHelper(0, -1, Tile.DOOR_OPEN, Orientation.NORTH, true);
    }

    @Test
    public void openDoorsSouthAreFeasible() {
        testHelper(0, 1, Tile.DOOR_OPEN, Orientation.SOUTH, true);
    }

    @Test
    public void openDoorsEastAreFeasible() {
        testHelper(1, 0, Tile.DOOR_OPEN, Orientation.EAST, true);
    }

    @Test
    public void openDoorsWestAreFeasible() {
        testHelper(-1, 0, Tile.DOOR_OPEN, Orientation.WEST, true);
    }

    @Test
    public void emptyTilesNorthAreFeasible() {
        testHelper(0, -1, Tile.EMPTY, Orientation.NORTH, true);
    }

    @Test
    public void emptyTilesSouthAreFeasible() {
        testHelper(0, 1, Tile.EMPTY, Orientation.SOUTH, true);
    }

    @Test
    public void emptyTilesEastAreFeasible() {
        testHelper(1, 0, Tile.EMPTY, Orientation.EAST, true);
    }

    @Test
    public void emptyTilesWestAreFeasible() {

        when(level.isDirectionFeasible(new Point(0, 1), Orientation.WEST)).thenReturn(true);
        testHelper(-1, 0, Tile.EMPTY, Orientation.WEST, true);
    }

    @Test
    public void widthOutsideRangeIsInfeasible() {
        when(level.getWidth()).thenReturn(2);
        boolean result = level.isDirectionFeasible(position, Orientation.EAST);

        assertFalse("Values outside range should be infeasible!", result);
    }

    @Test
    public void heightOutsideRangeIsInfeasible() {
        when(level.getWidth()).thenReturn(2);
        boolean result = level.isDirectionFeasible(position, Orientation.SOUTH);

        assertFalse("Values outside range should be infeasible!", result);
    }

    @Test
    public void xNegativeIsInfeasible() {
        position.x = 0;
        boolean result = level.isDirectionFeasible(position, Orientation.EAST);

        assertFalse("Values outside range should be infeasible!", result);
    }

    @Test
    public void yNegativeIsInfeasible() {
        position.y = 0;
        boolean result = level.isDirectionFeasible(position, Orientation.NORTH);

        assertFalse("Values outside range should be infeasible!", result);
    }
}
