package mouse.shared;

import mouse.server.simulation.Orientation;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by Florian on 2014-04-13.
 */
public class MouseState implements Serializable {

    private Point position;
    private Orientation orientation;

    public MouseState(Point position, Orientation orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    public Point getPosition() {
        return position;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MouseState that = (MouseState) o;

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
