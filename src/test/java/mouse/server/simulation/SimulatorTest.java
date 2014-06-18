package mouse.server.simulation;

import mouse.shared.Orientation;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimulatorTest {

    private SimulationMouse m1;
    private SimulationMouse m2;
    private SimulationMouse m3;
    private SimulationMouse m4;

    private Simulator simulator;

    @Before
    public void setUp() throws Exception {
        m1 = spy(new SimulationMouse(new Point(0, 0), null, 0));
        m2 = spy(new SimulationMouse(new Point(1, 0), null, 1));
        m3 = spy(new SimulationMouse(new Point(6, 6), null, 2));
        m4 = spy(new SimulationMouse(new Point(3, 3), null, 3));

        List<SimulationMouse> mice = new ArrayList<SimulationMouse>();
        mice.add(m1);
        mice.add(m2);
        mice.add(m3);
        mice.add(m4);

        doNothing().when(m1).confuse();
        doNothing().when(m2).confuse();
        doNothing().when(m3).confuse();
        doNothing().when(m4).confuse();

        simulator = new Simulator(mice);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLegitimateConfusionDirectlyNeighbouring() {
        doReturn(Orientation.EAST).when(m1).move();
        doReturn(Orientation.WEST).when(m2).move();
        doReturn(Orientation.WEST).when(m3).move();
        doReturn(Orientation.WEST).when(m4).move();

        simulator.simulate();

        verify(m1, times(1)).confuse();
        verify(m2, times(1)).confuse();
        verify(m3, never()).confuse();
        verify(m4, never()).confuse();
        verify(m1, never()).applyMotion(Orientation.EAST);
        verify(m2, never()).applyMotion(Orientation.WEST);
        verify(m3, times(1)).applyMotion(Orientation.WEST);
        verify(m4, times(1)).applyMotion(Orientation.WEST);
    }

    @Test
    public void testLegitimateConfusionNeighbouringAfterMove() {
        m2.applyMotion(Orientation.EAST);

        doReturn(Orientation.EAST).when(m1).move();
        doReturn(Orientation.WEST).when(m2).move();
        doReturn(Orientation.WEST).when(m3).move();
        doReturn(Orientation.WEST).when(m4).move();

        simulator.simulate();

        verify(m1, times(1)).confuse();
        verify(m2, times(1)).confuse();
        verify(m3, never()).confuse();
        verify(m4, never()).confuse();
        verify(m1, times(1)).applyMotion(Orientation.EAST);
        verify(m2, never()).applyMotion(Orientation.WEST);
        verify(m3, times(1)).applyMotion(Orientation.WEST);
        verify(m4, times(1)).applyMotion(Orientation.WEST);
    }
}
