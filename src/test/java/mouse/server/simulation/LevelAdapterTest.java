package mouse.server.simulation;

import mouse.shared.Orientation;
import java.awt.*;

import junit.framework.TestCase;
import mouse.shared.LevelStructure;
import mouse.shared.Tile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Markus Scherer, 8.4
 */
public class LevelAdapterTest extends TestCase {

    private LevelStructure levelStructure = null;
    private LevelAdapter adapter = null;
    private Point position = null;

    @Before
    public void setUp() throws Exception {
        levelStructure = mock(LevelStructure.class);
        adapter = new LevelAdapter(levelStructure);
        position = new Point(1, 1);
    }

    @After
    public void tearDown() throws Exception {
    }

    private void testHelper(int xDiff, int yDiff, Tile tileType, Orientation direction, boolean expected) {
        when(levelStructure.tileAt(position.x + xDiff, position.y + yDiff)).thenReturn(tileType);
        when(levelStructure.getHeight()).thenReturn(3);
        when(levelStructure.getWidth()).thenReturn(3);
        boolean result = adapter.isDirectionFeasible(position, direction);

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
        testHelper(-1, 0, Tile.EMPTY, Orientation.WEST, true);
    }

    @Test
    public void widthOutsideRangeIsInfeasible() {
        when(levelStructure.getWidth()).thenReturn(2);
        boolean result = adapter.isDirectionFeasible(position, Orientation.EAST);

        assertFalse("Values outside range should be infeasible!", result);
    }

    @Test
    public void heightOutsideRangeIsInfeasible() {
        when(levelStructure.getWidth()).thenReturn(2);
        boolean result = adapter.isDirectionFeasible(position, Orientation.SOUTH);

        assertFalse("Values outside range should be infeasible!", result);
    }

    @Test
    public void xNegativeIsInfeasible() {
        position.x = 0;
        boolean result = adapter.isDirectionFeasible(position, Orientation.EAST);

        assertFalse("Values outside range should be infeasible!", result);
    }

    @Test
    public void yNegativeIsInfeasible() {
        position.y = 0;
        boolean result = adapter.isDirectionFeasible(position, Orientation.NORTH);

        assertFalse("Values outside range should be infeasible!", result);
    }
}
