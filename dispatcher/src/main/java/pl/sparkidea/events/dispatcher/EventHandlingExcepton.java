package pl.sparkidea.events.dispatcher;

/**
 * @author Maciej Lesniak
 */
public class EventHandlingExcepton extends RuntimeException {

    public EventHandlingExcepton(String message) {
        super(message);
    }

    public EventHandlingExcepton(String message, Throwable cause) {
        super(message, cause);
    }
}
