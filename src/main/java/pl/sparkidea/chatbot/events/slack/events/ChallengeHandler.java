package pl.sparkidea.chatbot.events.slack.events;

import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import pl.sparkidea.chatbot.events.slack.dto.ChallengeResponse;
import pl.sparkidea.events.dispatcher.EventHandler;

import reactor.core.publisher.Mono;

/**
 * @author Maciej Lesniak
 */
@Component
public class ChallengeHandler implements EventHandler<JsonNode, ChallengeResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(ChallengeHandler.class);

    @Override
    public Mono<ChallengeResponse> handle(JsonNode event) {
        LOG.debug("Handling challenge event {}", event);
        return Mono.just(new ChallengeResponse(event.get(ChallengeResponse.CHALLENGE_FIELD).asText()));
    }
    
}
