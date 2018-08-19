package pl.sparkidea.chatbot.events.slack.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import pl.sparkidea.chatbot.config.SlackBotInfo;
import pl.sparkidea.chatbot.events.slack.dto.BotEventMessage;
import pl.sparkidea.chatbot.events.slack.dto.SlackEvent;
import pl.sparkidea.events.dispatcher.EventHandler;
import pl.sparkidea.events.predicates.JsonFieldPredicate;

import java.util.function.BiFunction;

import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

/**
 * @author Maciej Lesniak
 */
@Component
public class BotMessageHandler implements EventHandler<JsonNode, Void> {
    private static final Logger LOG = LoggerFactory.getLogger(BotMessageHandler.class);

    private static final String BOT_ID_JSON_PATH = SlackEvent.EVENT_FIELD + "." + BotEventMessage.BOT_ID_FIELD;
    private static final String EVENT_SUBTYPE_JSON_PATH = SlackEvent.EVENT_FIELD + "." + BotEventMessage.SUB_TYPE_FIELD;

    private final ObjectMapper objectMapper;
    private final String botId;

    private static final BiFunction<String, JsonNode, Mono<JsonNode>> IGNORE_OWN_MESSAGES = (botId, ev) -> {

        boolean isBotMessage = new JsonFieldPredicate(EVENT_SUBTYPE_JSON_PATH, jsonNode -> jsonNode.asText().equals("bot_message"))
                .and(new JsonFieldPredicate(BOT_ID_JSON_PATH, jsonNode -> jsonNode.asText().equals(botId)))
                .test(ev);

        if (isBotMessage) {
            LOG.debug(String.format("Own messages from bot [%s] are ignored", botId));
            return Mono.empty();
        }

        return Mono.just(ev);
    };

    public BotMessageHandler(ObjectMapper objectMapper,
                             SlackBotInfo slackBotInfo) {
        this.objectMapper = objectMapper;
        this.botId = slackBotInfo.getBotId();
    }

    @Override
    public Mono<Void> handle(@NonNull JsonNode event) {
        return Mono.just(event)
                .doOnSuccess(jsonNode -> LOG.debug("Verifying BOT event: {}", jsonNode))
                .flatMap(jsonNode -> IGNORE_OWN_MESSAGES.apply(botId, jsonNode))
                .map(jsonNode -> objectMapper.convertValue(jsonNode, SlackEvent.class))
                .flatMap(slackEvent -> {
                    LOG.debug("Handling bot event: {}", slackEvent);
                    return Mono.empty();
                });
    }
}
