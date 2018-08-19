package pl.sparkidea.chatbot.events.slack.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

/**
 * @author Maciej Lesniak
 */

public class BotEventMessage extends AbstractEventMessage {

    public static final String CHANNEL_TYPE_FIELD = "channel_type";
    public static final String SUB_TYPE_FIELD = "subtype";
    public static final String BOT_ID_FIELD = "bot_id";
    public static final String USER_NAME_FIELD = "username";

    @JsonProperty(SUB_TYPE_FIELD)
    private String subType;
    @JsonProperty(CHANNEL_TYPE_FIELD)
    private String channelType;
    @JsonProperty(BOT_ID_FIELD)
    private String botId;
    @JsonProperty(USER_NAME_FIELD)
    private String username;

    @JsonCreator
    public BotEventMessage(@JsonProperty(TYPE_FIELD) String type,
                           @JsonProperty(TIMESTAMP_FIELD) Instant timestamp,
                           @JsonProperty(EVENT_TIMESTAMP_FIELD) Instant eventTime,
                           @JsonProperty(TEXT_FIELD) String text,
                           @JsonProperty(CHANNEL_ID__FIELD) String channelId,
                           @JsonProperty(SUB_TYPE_FIELD) String subType,
                           @JsonProperty(CHANNEL_TYPE_FIELD) String channelType,
                           @JsonProperty(BOT_ID_FIELD) String botId,
                           @JsonProperty(USER_NAME_FIELD) String username) {
        super(type, timestamp, eventTime, text, channelId);
        this.subType = subType;
        this.channelType = channelType;
        this.botId = botId;
        this.username = username;
    }

    public String getSubType() {
        return subType;
    }

    public String getChannelType() {
        return channelType;
    }

    public String getBotId() {
        return botId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "BotMessage{" +
                "subType='" + subType + '\'' +
                ", channelType='" + channelType + '\'' +
                ", botId='" + botId + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BotEventMessage)) return false;
        if (!super.equals(o)) return false;
        BotEventMessage that = (BotEventMessage) o;
        return Objects.equals(subType, that.subType) &&
                Objects.equals(channelType, that.channelType) &&
                Objects.equals(botId, that.botId) &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subType, channelType, botId, username);
    }
}
