package mouse.shared;

/**
 * Describes the types of tiles form a level.
 * User: Markus
 * Date: 02.04.14
 */
public enum Tile {
	EMPTY, WALL, DOOR_OPEN, DOOR_CLOSED;

    public static boolean isDoor(Tile tile){
        return tile == DOOR_OPEN || tile == DOOR_CLOSED;
    }
}
