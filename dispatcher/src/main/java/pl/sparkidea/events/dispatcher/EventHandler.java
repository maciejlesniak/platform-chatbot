package pl.sparkidea.events.dispatcher;

import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

/**
 * @author Maciej Lesniak
 */
@FunctionalInterface
public interface EventHandler<T, R> {
    Mono<R> handle(@NonNull T event);
}
