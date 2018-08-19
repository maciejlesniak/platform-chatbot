package pl.sparkidea.events.dispatcher;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import reactor.core.publisher.Mono;

/**
 * @author Maciej Lesniak
 */
public abstract class AbstractEventDispatcher<T> implements EventDispatcher<T> {

    private static final Logger LOG = Logger.getLogger(AbstractEventDispatcher.class.getName());

    private final Map<Predicate<T>, EventHandler<T, ?>> mappings;
    private final EventHandler<T, ?> defaultHandler;

    public AbstractEventDispatcher(EventHandler<T, ?> defaultHandler) {
        this.defaultHandler = defaultHandler;
        this.mappings = new HashMap<>();
    }

    public AbstractEventDispatcher() {
        this(event -> {
            LOG.info(String.format("Default handler invoked for event: %s", event));
            return Mono.empty();
        });
    }

    public AbstractEventDispatcher<T> forPredicate(Predicate<T> predicate, EventHandler<T, ?> handler) {
        this.mappings.put(predicate, handler);
        return this;
    }

    @Override
    public EventHandler<T, ?> dispatch(final T event) {
        Optional<EventHandler<T, ?>> handler = mappings.entrySet().stream()
                .filter(predicateEventHandlerEntry -> predicateEventHandlerEntry.getKey().test(event))
                .findAny()
                .map(Map.Entry::getValue);

        return handler.orElse(defaultHandler);
    }

}
