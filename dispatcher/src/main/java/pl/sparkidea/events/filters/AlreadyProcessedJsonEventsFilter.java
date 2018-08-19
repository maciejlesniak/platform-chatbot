package pl.sparkidea.events.filters;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.logging.Logger;

import reactor.core.publisher.Mono;

/**
 * @author Maciej Lesniak
 */
public final class AlreadyProcessedJsonEventsFilter implements MonoJsonNodeFunction {

    private final static Logger LOG = Logger.getLogger(AlreadyProcessedJsonEventsFilter.class.getName());

    private final ConcurrentMap<String, Object> processedCache;
    private final int maxCacheSize;
    private final Function<JsonNode, Optional<String>> fingerprintFunction;

    public AlreadyProcessedJsonEventsFilter(ConcurrentMap<String, Object> processedCache,
                                            int maxCacheSize,
                                            Function<JsonNode, Optional<String>> fingerprintFunction) {
        this.processedCache = processedCache;
        this.maxCacheSize = maxCacheSize;
        this.fingerprintFunction = fingerprintFunction;
    }

    @Override
    public Mono<JsonNode> apply(JsonNode ev) {
        final Optional<String> evFingerprint = fingerprintFunction.apply(ev);

        if (!evFingerprint.isPresent()) {
            return Mono.just(ev);
        }

        final boolean isProcessed = processedCache.keySet().stream()
                .anyMatch(cachedFingerPrint -> cachedFingerPrint.equals(evFingerprint.get()));

        if (processedCache.size() > maxCacheSize) {
            processedCache.clear();
        }

        if (maxCacheSize > 0 && isProcessed) {
            LOG.finer(String.format("Event with fingerprint [%s] has been already processed", evFingerprint));
            return Mono.empty();
        }

        processedCache.put(evFingerprint.get(), new Object());
        return Mono.just(ev);
    }

}
