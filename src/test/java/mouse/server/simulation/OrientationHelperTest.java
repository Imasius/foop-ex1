package mouse.server.simulation;

import mouse.shared.Orientation;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrientationHelperTest extends TestCase {

    OrientationHelper helper;
    List<Orientation> results;

    @Before
    @Override
    public void setUp() throws Exception {
        helper = new OrientationHelper();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        helper = null;
        results = null;
    }

    @Test
    public void checkNonAmbigousNorth() {
        Point start = new Point(5, 5);
        Point dest = new Point(start.x, start.y - 1);

        results = helper.getViableOrientations(start, dest);

        assertEquals("failure - result not unambigous", results.size(), 1);
        assertThat(results, hasItem(Orientation.NORTH));
    }

    @Test
    public void checkNonAmbigousSouth() {
        Point start = new Point(5, 5);
        Point dest = new Point(start.x, start.y + 1);

        results = helper.getViableOrientations(start, dest);

        assertEquals("failure - result not unambigous", results.size(), 1);
        assertThat(results, hasItem(Orientation.SOUTH));
    }

    @Test
    public void checkNonAmbigousEast() {
        Point start = new Point(5, 5);
        Point dest = new Point(start.x + 1, start.y);

        results = helper.getViableOrientations(start, dest);

        assertEquals("failure - result not unambigous", results.size(), 1);
        assertThat(results, hasItem(Orientation.EAST));
    }

    @Test
    public void checkNonAmbigousWest() {
        Point start = new Point(5, 5);
        Point dest = new Point(start.x - 1, start.y);

        results = helper.getViableOrientations(start, dest);

        assertEquals("failure - result not unambigous", results.size(), 1);
        assertThat(results, hasItem(Orientation.WEST));
    }

    @Test
    public void checkAmbigousNorthNorthWest() {
        Point start = new Point(5, 5);
        Point dest = new Point(start.x - 1, start.y - 2);

        results = helper.getViableOrientations(start, dest);

        assertEquals("failure - two directions expected", results.size(), 2);
        assertThat(results, equalTo(Arrays.asList(Orientation.NORTH, Orientation.WEST)));
    }

    @Test
    public void checkAmbigousNorthWestWest() {
        Point start = new Point(5, 5);
        Point dest = new Point(start.x - 2, start.y - 1);

        results = helper.getViableOrientations(start, dest);

        assertEquals("failure - two directions expected", results.size(), 2);
        assertThat(results, equalTo(Arrays.asList(Orientation.WEST, Orientation.NORTH)));
    }

    @Test
    public void checkEmptyOnSamePosition() {
        Point start = new Point(5, 5);
        Point dest = new Point(start);

        results = helper.getViableOrientations(start, dest);

        assertEquals("failure - expected empty result", results.size(), 0);
    }
}
