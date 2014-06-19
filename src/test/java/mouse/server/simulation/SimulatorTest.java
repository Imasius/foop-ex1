package mouse.server.simulation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import mouse.shared.Orientation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;

public class SimulatorTest {
    
    private SimulationMouse m1;
    private SimulationMouse m2;
    private SimulationMouse m3;
    private SimulationMouse m4;
    
    private Simulator simulator;
    @Mock
    private SimulationLevel level;
    private List<SimulationMouse> mice;
    
    @Before
    public void setUp() throws Exception {
        m1 = spy(new SimulationMouse(new Point(0, 0), Orientation.EAST, 0));
        m2 = spy(new SimulationMouse(new Point(1, 0), Orientation.WEST, 1));
        m3 = spy(new SimulationMouse(new Point(6, 6), Orientation.NORTH, 2));
        m4 = spy(new SimulationMouse(new Point(3, 3), Orientation.SOUTH, 3));
        
        mice = new ArrayList<SimulationMouse>();
        mice.add(m1);
        mice.add(m2);
        mice.add(m3);
        mice.add(m4);
        
        doNothing().when(m1).confuse();
        doNothing().when(m2).confuse();
        doNothing().when(m3).confuse();
        doNothing().when(m4).confuse();
        
        simulator = new Simulator();
        
        MockitoAnnotations.initMocks(this);
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testLegitimateConfusionDirectlyNeighbouring() {
        doReturn(Orientation.EAST).when(simulator).getNewDirection(m1, level);
        doReturn(Orientation.WEST).when(simulator).getNewDirection(m2, level);
        doReturn(Orientation.WEST).when(simulator).getNewDirection(m3, level);
        doReturn(Orientation.WEST).when(simulator).getNewDirection(m4, level);
        
        simulator.simulate(mice, level);
        
        verify(m1, times(1)).confuse();
        verify(m2, times(1)).confuse();
        verify(m3, never()).confuse();
        verify(m4, never()).confuse();
    }
    
    @Test
    public void testLegitimateConfusionNeighbouringAfterMove() {
        Point p = m1.getPosition();
        Orientation o = m1.getOrientation();
        doReturn(false).when(level).isDirectionFeasible(p, o);
        
        simulator.simulate(mice, level);
        simulator.simulate(mice, level);
        
        verify(m1, times(2)).confuse();
        verify(m2, times(1)).confuse();
        verify(m3, times(1)).confuse();
        verify(m4, times(1)).confuse();
        verify(m3, times(1)).sniff();
        verify(m4, times(1)).sniff();
    }
}
