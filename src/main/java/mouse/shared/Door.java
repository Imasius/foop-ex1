package mouse.shared;

import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author kevin_000
 */
public class Door implements Serializable {

    private Point position;
    private boolean closed;

    public Door(Point position, boolean closed) {
        this.position = position;
        this.closed = closed;
    }

    /**
     * @return the position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * @return the closed
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * @return if 
     */
    public boolean isOpen() {
        return !closed;
    }

    /**
     * @param closed the closed to set
     */
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Door other = (Door) obj;
        if (this.position != other.position && (this.position == null || !this.position.equals(other.position))) {
            return false;
        }
        if (this.closed != other.closed) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Door{"
                + "position=" + position
                + ", closed=" + closed
                + '}';
    }
}
