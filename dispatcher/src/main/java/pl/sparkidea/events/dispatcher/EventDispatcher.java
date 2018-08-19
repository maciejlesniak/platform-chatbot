package pl.sparkidea.events.dispatcher;

/**
 * @author Maciej Lesniak
 */
public interface EventDispatcher<E> {
    EventHandler<E, ? extends Object> dispatch(final E event);
}
