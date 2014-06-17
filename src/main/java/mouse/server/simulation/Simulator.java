package mouse.server.simulation;

import mouse.shared.Orientation;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mouse.server.simulation.event.GameLogicEventListener;

/**
 * Simulates the Game Logic. User: Markus Date: 05.05.14
 */
public class Simulator {

    private List<Mouse> mice;
    private final ArrayList<GameLogicEventListener> listeners = new ArrayList<GameLogicEventListener>();

    public Simulator(List<Mouse> mice) {
        this.mice = mice;
    }

    public List<mouse.shared.Mouse> simulate() {
        Set<Mouse> confusedMice = new HashSet<Mouse>();
        List<mouse.shared.Mouse> states = new ArrayList<mouse.shared.Mouse>();

        for (Mouse m1 : mice) {
            if (confusedMice.contains(m1)) {
                states.add(new mouse.shared.Mouse(m1.getPosition(), m1.getLastOrientation()));
                continue;
            }

            Orientation direction = m1.move();
            Point target = OrientationHelper.getInstance().applyOrientation(m1.getPosition(), direction);

            for (Mouse m2 : mice) {
                if (m1 == m2) {
                    continue;
                }
                if (m2.getPosition().equals(target)) {
                    confusedMice.add(m1);
                    confusedMice.add(m2);
                }
            }

            if (!confusedMice.contains(m1)) {
                m1.applyMotion(direction);
            }

            states.add(new mouse.shared.Mouse(m1.getPosition(), direction));
        }

        for (Mouse m : confusedMice) {
            m.confuse();
        }

        return states;
    }

    public void registerGameLogicEventListener(GameLogicEventListener l) {
        listeners.add(l);
    }
}
