package mouse.server.simulation;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simulates the Game Logic.
 * User: Markus
 * Date: 05.05.14
 */
public class Simulator {
	private List<Mouse> mice;

	public Simulator(List<Mouse> mice) {
		this.mice = mice;
	}

	public void simulate() {
		Set<Mouse> confusedMice = new HashSet<Mouse>();
		
		for(Mouse m1 : mice) {
			if(confusedMice.contains(m1)) {
				continue;
			}
			
			Orientation direction = m1.move();
			Point target = OrientationHelper.getInstance().applyOrientation(m1.getPosition(), direction);
			
			for(Mouse m2 : mice) {
				if(m1 == m2) {
					continue;
				}
				if(m2.getPosition().equals(target)) {			
					confusedMice.add(m1);
					confusedMice.add(m2);
				}
			}
			
			if(! confusedMice.contains(m1)) {
				m1.applyMotion(direction);
			}
		}
		
		for(Mouse m : confusedMice) {
			m.confuse();
		}
	}
}
