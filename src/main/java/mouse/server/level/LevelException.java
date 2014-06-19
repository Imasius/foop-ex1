package mouse.server.level;

/**
 * User: Simon Date: 17.06.2014
 */
public class LevelException extends RuntimeException {

    public LevelException() {
        super();
    }

    public LevelException(String message) {
        super(message);
    }

    public LevelException(String message, Throwable cause) {
        super(message, cause);
    }

    public LevelException(Throwable cause) {
        super(cause);
    }
}
