package pl.sparkidea.events.filters;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.function.Function;

import reactor.core.publisher.Mono;

/**
 * @author Maciej Lesniak
 */
public interface MonoJsonNodeFunction extends Function<JsonNode, Mono<JsonNode>> {
}
