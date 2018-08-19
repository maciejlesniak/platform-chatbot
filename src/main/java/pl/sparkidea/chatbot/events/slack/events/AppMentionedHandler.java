package pl.sparkidea.chatbot.events.slack.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import pl.sparkidea.chatbot.events.slack.dto.AppMentionedEventMessage;
import pl.sparkidea.chatbot.events.slack.dto.SlackEvent;
import pl.sparkidea.chatbot.events.slack.services.EchoReplyService;
import pl.sparkidea.events.dispatcher.EventHandler;

import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

/**
 * @author Maciej Lesniak
 */
@Component
public class AppMentionedHandler implements EventHandler<JsonNode, Void> {
    private static final Logger LOG = LoggerFactory.getLogger(CallbackMessageHandler.class);


    private final ObjectMapper objectMapper;
    private final EchoReplyService echoReplyService;

    public AppMentionedHandler(ObjectMapper objectMapper,
                               EchoReplyService echoReplyService) {
        this.objectMapper = objectMapper;
        this.echoReplyService = echoReplyService;
    }

    @Override
    public Mono<Void> handle(@NonNull JsonNode event) {
        return Mono.just(event)
                .doOnSuccess(jsonNode -> LOG.debug("Verifying APP MENTIONED event: {}", jsonNode))
                .map(jsonNode -> objectMapper.convertValue(jsonNode, SlackEvent.class))
                .flatMap(slackEvent -> {
                    LOG.debug("Handling callback/app mentioned event: {}", slackEvent);

                    final AppMentionedEventMessage message = objectMapper.convertValue(slackEvent.getEventMessage(), AppMentionedEventMessage.class);
                    echoReplyService.replyToMessage(message);

                    return Mono.empty();
                });
    }
}
