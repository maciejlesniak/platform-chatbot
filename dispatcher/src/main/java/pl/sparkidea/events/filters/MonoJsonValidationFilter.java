package pl.sparkidea.events.filters;

import com.fasterxml.jackson.databind.JsonNode;

import pl.sparkidea.events.dispatcher.EventSecurityException;

import java.util.function.Predicate;

import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

/**
 * @author Maciej Lesniak
 */
public final class MonoJsonValidationFilter implements MonoJsonNodeFunction {

    private final Predicate<JsonNode> validator;
    private final String errorMessage;

    public MonoJsonValidationFilter(@NonNull Predicate<JsonNode> validator, @NonNull String errorMessage) {
        this.validator = validator;
        this.errorMessage = errorMessage;
    }

    @Override
    public Mono<JsonNode> apply(JsonNode ev) {
        return Mono
                .just(validator.test(ev))
                .flatMap(aBoolean -> !aBoolean ? Mono.error(new EventSecurityException(errorMessage)) : Mono.just(ev));
    }
}
