package pl.sparkidea.events.dispatcher;

/**
 * @author Maciej Lesniak
 */
public class EventSecurityException extends EventHandlingExcepton {

    public EventSecurityException(String message) {
        super(message);
    }
}
