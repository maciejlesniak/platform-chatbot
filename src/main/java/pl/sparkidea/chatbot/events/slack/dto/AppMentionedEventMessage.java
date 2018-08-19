package pl.sparkidea.chatbot.events.slack.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

/**
 * @author Maciej Lesniak
 */
public class AppMentionedEventMessage extends AbstractEventMessage {

    public static final String USER_FIELD = "user";
    public static final String CLIENT_MSG_ID_FIELD = "client_msg_id";

    @JsonProperty(USER_FIELD)
    private String user;
    @JsonProperty(CLIENT_MSG_ID_FIELD)
    private String messageId;

    @JsonCreator
    public AppMentionedEventMessage(@JsonProperty(TYPE_FIELD) String type,
                                    @JsonProperty(TIMESTAMP_FIELD) Instant timestamp,
                                    @JsonProperty(EVENT_TIMESTAMP_FIELD) Instant eventTime,
                                    @JsonProperty(TEXT_FIELD) String text,
                                    @JsonProperty(CHANNEL_ID__FIELD) String channelId,
                                    @JsonProperty(USER_FIELD) String user,
                                    @JsonProperty(CLIENT_MSG_ID_FIELD) String messageId) {
        super(type, timestamp, eventTime, text, channelId);
        this.user = user;
        this.messageId = messageId;
    }

    public String getUser() {
        return user;
    }

    public String getMessageId() {
        return messageId;
    }

    @Override
    public String toString() {
        return "AppMentionedMessage{" +
                "user='" + user + '\'' +
                ", messageId='" + messageId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppMentionedEventMessage)) return false;
        if (!super.equals(o)) return false;
        AppMentionedEventMessage that = (AppMentionedEventMessage) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, messageId);
    }
}
