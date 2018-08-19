package pl.sparkidea.chatbot.events.slack.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import pl.sparkidea.events.dispatcher.EventDispatcher;
import pl.sparkidea.events.filters.AlreadyProcessedJsonEventsFilter;
import pl.sparkidea.events.filters.MonoJsonValidationFilter;

import java.io.IOException;
import java.util.Optional;

import reactor.core.publisher.Mono;

/**
 * Routes incoming HTTP request to proper handler
 *
 * @author Maciej Lesniak
 */
@Component
public class SlackHttpRequestRouter implements HandlerFunction<ServerResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(SlackHttpRequestRouter.class);

    private final ObjectMapper objectMapper;
    private final EventDispatcher<JsonNode> eventDispatcher;
    private final MonoJsonValidationFilter credentialsMonoJsonValidationFilter;
    private final AlreadyProcessedJsonEventsFilter alreadyProcessedJsonEventsFilter;

    public SlackHttpRequestRouter(ObjectMapper objectMapper,
                                  EventDispatcher<JsonNode> slackEventDispatcher,
                                  MonoJsonValidationFilter credentialsMonoJsonValidationFilter,
                                  AlreadyProcessedJsonEventsFilter alreadyProcessedJsonEventsFilter) {
        this.objectMapper = objectMapper;
        this.eventDispatcher = slackEventDispatcher;
        this.credentialsMonoJsonValidationFilter = credentialsMonoJsonValidationFilter;
        this.alreadyProcessedJsonEventsFilter = alreadyProcessedJsonEventsFilter;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(String.class)
                .map(obj -> {
                    try {
                        return objectMapper.readTree(obj);
                    } catch (IOException ex) {
                        throw new IllegalArgumentException("Cannot parse server request. Is it JSON format?", ex);
                    }
                })
                .flatMap(alreadyProcessedJsonEventsFilter)
                .flatMap(credentialsMonoJsonValidationFilter)
                .flatMap(jsonNode -> Optional.ofNullable(jsonNode)
                        .map(payload -> eventDispatcher.dispatch(payload).handle(payload))
                        .orElse(Mono.empty())
                )
                // todo switch empty
                .flatMap(response ->
                        Optional.ofNullable(response)
                                .map(responseBodyObj -> ServerResponse.status(HttpStatus.OK).syncBody(responseBodyObj))
                                .orElse(ServerResponse.status(HttpStatus.OK).build())
                )
                .doOnSuccess(response -> LOG.debug("Handled request [{}] with <<{}>> response", serverRequest, response))
                .doOnError(throwable -> LOG.error("Failed to handle request [{}] due to exception <<{}>>", serverRequest, throwable.getMessage()));
    }

}


