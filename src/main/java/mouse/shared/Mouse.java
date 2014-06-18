package mouse.shared;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by Florian on 2014-04-13. Adapted by Kevin on 20014-07-18
 */
public class Mouse implements Serializable {

    //A mouse always has a position, either for simulation or for visualization
    private Point position;
    //A mouse always has a orientation either for simulation or visualization
    private Orientation orientation;
    //On both server and client a mouse needs an identifier to share. Mice 
    //can be exchanged as subset of all mice on an update and a mapping back to the
    //original mouse is needed.
    private int playerIndex;

    public Mouse(Point position, Orientation orientation, int playerIndex) {
        this.position = position;
        this.orientation = orientation;
    }

    public Point getPosition() {
        return position;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Mouse that = (Mouse) o;

        if (orientation != that.orientation) {
            return false;
        }
        if (!position.equals(that.position)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + orientation.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MouseState{"
                + "position=" + position
                + ", orientation=" + orientation
                + '}';
    }
}
