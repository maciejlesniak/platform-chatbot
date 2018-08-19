package pl.sparkidea.chatbot.config;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import pl.sparkidea.chatbot.events.slack.dto.BotEventMessage;
import pl.sparkidea.chatbot.events.slack.dto.CallbackEventMessage;
import pl.sparkidea.chatbot.events.slack.dto.ChallengeResponse;
import pl.sparkidea.chatbot.events.slack.dto.SlackEvent;
import pl.sparkidea.chatbot.events.slack.events.AppMentionedHandler;
import pl.sparkidea.chatbot.events.slack.events.BotMessageHandler;
import pl.sparkidea.chatbot.events.slack.events.CallbackMessageHandler;
import pl.sparkidea.chatbot.events.slack.events.ChallengeHandler;
import pl.sparkidea.chatbot.events.slack.http.SlackEventDispatcherImpl;
import pl.sparkidea.events.dispatcher.EventDispatcher;
import pl.sparkidea.events.predicates.JsonFieldPredicate;

import java.util.function.Predicate;

/**
 * @author Maciej Lesniak
 */
@Configuration
public class RouteConfig {
    private static final String SLACK_ENDPOINT = "/apis/slack";

    private static final String EVENT_TYPE_JSON_PATH = SlackEvent.EVENT_FIELD + "." + CallbackEventMessage.TYPE_FIELD;
    private static final String MESSAGE_SUB_TYPE_JSON_PATH = SlackEvent.EVENT_FIELD + "." + BotEventMessage.SUB_TYPE_FIELD;
    private static final String CHANNEL_TYPE_JSON_PATH = SlackEvent.EVENT_FIELD + "." + CallbackEventMessage.CHANNEL_TYPE_FIELD;

    private static final String EVENT_CALLBACK_FIELD_VALUE = "event_callback";
    private static final String APP_MENTION_FIELD_VALUE = "app_mention";
    private static final String MESSAGE_FIELD_VALUE = "message";
    private static final String IM_FIELD_VALUE = "im";
    private static final String BOT_MESSAGE_FIELD_VALUE = "bot_message";

    private static final Predicate<JsonNode> IS_CALLBACK_EVENT = new JsonFieldPredicate(SlackEvent.TYPE_FIELD, val -> val.asText().equals(EVENT_CALLBACK_FIELD_VALUE));
    private static final Predicate<JsonNode> IS_APP_MENTIONED = new JsonFieldPredicate(EVENT_TYPE_JSON_PATH, val -> val.asText().equals(APP_MENTION_FIELD_VALUE));
    private static final Predicate<JsonNode> IS_CHANNEL_TYPE_MISSING = new JsonFieldPredicate(CHANNEL_TYPE_JSON_PATH, JsonFieldPredicate.MISSING_FIELD);
    private static final Predicate<JsonNode> IS_MESSAGE_SUBTYPE_MISSING = new JsonFieldPredicate(MESSAGE_SUB_TYPE_JSON_PATH, JsonFieldPredicate.MISSING_FIELD);
    private static final Predicate<JsonNode> IS_MESSAGE = new JsonFieldPredicate(EVENT_TYPE_JSON_PATH, val -> val.asText().equals(MESSAGE_FIELD_VALUE));
    private static final Predicate<JsonNode> IS_IM_MESSAGE = new JsonFieldPredicate(CHANNEL_TYPE_JSON_PATH, val -> val.asText().equals(IM_FIELD_VALUE));
    private static final Predicate<JsonNode> IS_BOT_MESSAGE = new JsonFieldPredicate(MESSAGE_SUB_TYPE_JSON_PATH, val -> val.asText().equals(BOT_MESSAGE_FIELD_VALUE));

    private static final Predicate<JsonNode> URL_VERIFICATION_PREDICATE = new JsonFieldPredicate(ChallengeResponse.CHALLENGE_FIELD);
    private static final Predicate<JsonNode> EVENT_APP_MENTIONED_PREDICATE = IS_APP_MENTIONED.and(IS_CHANNEL_TYPE_MISSING).and(IS_MESSAGE_SUBTYPE_MISSING).and(IS_CALLBACK_EVENT);
    private static final Predicate<JsonNode> EVENT_CALLBACK_PREDICATE = IS_IM_MESSAGE.and(IS_BOT_MESSAGE.negate()).and(IS_MESSAGE_SUBTYPE_MISSING).and(IS_CALLBACK_EVENT);
    private static final Predicate<JsonNode> EVENT_BOT_MESSAGE_PREDICATE = IS_BOT_MESSAGE.and(IS_MESSAGE).and(IS_IM_MESSAGE).and(IS_CALLBACK_EVENT);

    // todo global exception handler with proper status code

    @Bean
    public RouterFunction<ServerResponse> route(HandlerFunction<ServerResponse> slackRequestHandler) {
        return RouterFunctions.route(RequestPredicates
                        .POST(SLACK_ENDPOINT)
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                slackRequestHandler);
    }

    @Bean
    public EventDispatcher<JsonNode> slackEventDispatcher(CallbackMessageHandler callbackMessageHandler,
                                                          ChallengeHandler challengeHandler,
                                                          AppMentionedHandler appMentionedHandler,
                                                          BotMessageHandler botMessageHandler) {
        return new SlackEventDispatcherImpl()
                .forPredicate(URL_VERIFICATION_PREDICATE, challengeHandler)
                .forPredicate(EVENT_CALLBACK_PREDICATE, callbackMessageHandler)
                .forPredicate(EVENT_APP_MENTIONED_PREDICATE, appMentionedHandler)
                .forPredicate(EVENT_BOT_MESSAGE_PREDICATE, botMessageHandler);
    }


}
