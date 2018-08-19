package pl.sparkidea.chatbot.events.slack.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import pl.sparkidea.chatbot.events.slack.dto.CallbackEventMessage;
import pl.sparkidea.chatbot.events.slack.dto.SlackEvent;
import pl.sparkidea.chatbot.events.slack.services.EchoReplyService;
import pl.sparkidea.events.dispatcher.EventHandler;

import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

/**
 * @author Maciej Lesniak
 */
@Component
public class CallbackMessageHandler implements EventHandler<JsonNode, Void> {
    private static final Logger LOG = LoggerFactory.getLogger(CallbackMessageHandler.class);
    private static final String REPLY_TEMPLATE = "<@%s> Auto reply for: %s";

    private final ObjectMapper objectMapper;
    private final EchoReplyService echoReplyService;


    public CallbackMessageHandler(ObjectMapper objectMapper,
                                  EchoReplyService echoReplyService) {
        this.objectMapper = objectMapper;
        this.echoReplyService = echoReplyService;
    }


    @Override
    public Mono<Void> handle(@NonNull JsonNode event) {
        return Mono.just(event)
                .doOnSuccess(jsonNode -> LOG.debug("Verifying CALLBACK event: {}", jsonNode))
                .map(jsonNode -> objectMapper.convertValue(jsonNode, SlackEvent.class))
                .flatMap(slackEvent -> {
                    LOG.debug("Handling callback/message event: {}", slackEvent);

                    final CallbackEventMessage message = objectMapper.convertValue(slackEvent.getEventMessage(), CallbackEventMessage.class);
                    echoReplyService.replyToMessage(message);

                    return Mono.empty();
                });

    }

}
