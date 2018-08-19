package pl.sparkidea.chatbot.events.slack.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

/**
 * @author Maciej Lesniak
 */
public abstract class AbstractEventMessage implements ChanelledMessage {

    public static final String TYPE_FIELD = "type";
    public static final String TEXT_FIELD = "text";
    public static final String TIMESTAMP_FIELD = "ts";
    public static final String CHANNEL_ID__FIELD = "channel";
    public static final String EVENT_TIMESTAMP_FIELD = "event_ts";


    @JsonProperty(TYPE_FIELD)
    private String type;
    @JsonProperty(TIMESTAMP_FIELD)
    private Instant timestamp;
    @JsonProperty(EVENT_TIMESTAMP_FIELD)
    private Instant eventTime;
    @JsonProperty(TEXT_FIELD)
    private String text;
    @JsonProperty(CHANNEL_ID__FIELD)
    private String channelId;

    @JsonCreator
    AbstractEventMessage(@JsonProperty(TYPE_FIELD) String type,
                         @JsonProperty(TIMESTAMP_FIELD) Instant timestamp,
                         @JsonProperty(EVENT_TIMESTAMP_FIELD) Instant eventTime,
                         @JsonProperty(TEXT_FIELD) String text,
                         @JsonProperty(CHANNEL_ID__FIELD) String channelId) {
        this.type = type;
        this.timestamp = timestamp;
        this.eventTime = eventTime;
        this.text = text;
        this.channelId = channelId;
    }

    public String getType() {
        return type;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Instant getEventTime() {
        return eventTime;
    }

    public String getText() {
        return text;
    }

    @Override
    public String getChannelId() {
        return channelId;
    }

    @Override
    public String toString() {
        return "AbstractMessage{" +
                "type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", eventTime=" + eventTime +
                ", text='" + text + '\'' +
                ", channelId='" + channelId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEventMessage)) return false;
        AbstractEventMessage that = (AbstractEventMessage) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(eventTime, that.eventTime) &&
                Objects.equals(text, that.text) &&
                Objects.equals(channelId, that.channelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, timestamp, eventTime, text, channelId);
    }
}
