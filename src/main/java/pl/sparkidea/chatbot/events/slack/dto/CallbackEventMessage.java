package pl.sparkidea.chatbot.events.slack.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

/**
 * @author Maciej Lesniak
 */
public class CallbackEventMessage extends AbstractEventMessage {

    public static final String CHANNEL_TYPE_FIELD = "channel_type";
    public static final String CLIENT_MSG_ID_FIELD = "client_msg_id";
    public static final String USER_FIELD = "user";

    @JsonProperty(CHANNEL_TYPE_FIELD)
    private String channelType;
    @JsonProperty(CLIENT_MSG_ID_FIELD)
    private String clientMessageId;
    @JsonProperty(USER_FIELD)
    private String user;

    @JsonCreator
    public CallbackEventMessage(@JsonProperty(TYPE_FIELD) String type,
                                @JsonProperty(TIMESTAMP_FIELD) Instant timestamp,
                                @JsonProperty(EVENT_TIMESTAMP_FIELD) Instant eventTime,
                                @JsonProperty(TEXT_FIELD) String text,
                                @JsonProperty(CHANNEL_ID__FIELD) String channelId,
                                @JsonProperty(CHANNEL_TYPE_FIELD) String channelType,
                                @JsonProperty(CLIENT_MSG_ID_FIELD) String clientMessageId,
                                @JsonProperty(USER_FIELD) String user) {
        super(type, timestamp, eventTime, text, channelId);
        this.channelType = channelType;
        this.clientMessageId = clientMessageId;
        this.user = user;
    }

    public String getChannelType() {
        return channelType;
    }

    public String getClientMessageId() {
        return clientMessageId;
    }

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "CallbackMessage{" +
                "channelType='" + channelType + '\'' +
                ", clientMessageId='" + clientMessageId + '\'' +
                ", user='" + user + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CallbackEventMessage)) return false;
        if (!super.equals(o)) return false;
        CallbackEventMessage that = (CallbackEventMessage) o;
        return Objects.equals(channelType, that.channelType) &&
                Objects.equals(clientMessageId, that.clientMessageId) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), channelType, clientMessageId, user);
    }
}
