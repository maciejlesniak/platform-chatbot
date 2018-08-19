package pl.sparkidea.chatbot.events.slack.validators;

import com.fasterxml.jackson.databind.JsonNode;

import pl.sparkidea.chatbot.events.slack.dto.SlackEvent;

import java.util.Optional;
import java.util.function.Predicate;

import javax.validation.constraints.NotNull;

/**
 * @author Maciej Lesniak
 */
public class MessageCredentialsValidator implements Predicate<JsonNode> {

    private final String appId;
    private final String verificationToken;

    public MessageCredentialsValidator(@NotNull String appId,
                                       @NotNull String verificationToken) {
        this.appId = appId;
        this.verificationToken = verificationToken;
    }

    @Override
    public boolean test(JsonNode event) {
        String messageAppId = Optional.ofNullable(event.get(SlackEvent.API_APP_ID_FIELD)).map(JsonNode::asText).orElse(null);
        String messageToken = Optional.ofNullable(event.get(SlackEvent.TOKEN_FIELD)).map(JsonNode::asText).orElse(null);
        return appId.equals(messageAppId) && verificationToken.equals(messageToken);
    }
}
