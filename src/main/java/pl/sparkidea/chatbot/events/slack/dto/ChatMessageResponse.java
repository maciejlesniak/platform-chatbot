package pl.sparkidea.chatbot.events.slack.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author Maciej Lesniak
 */
public class ChatMessageResponse {

    public final static String TEXT_FIELD = "text";
    public final static String CHANNEL_FIELD = "channel";

    @JsonProperty(TEXT_FIELD)
    private final String text;
    @JsonProperty(CHANNEL_FIELD)
    private final String channel;

    @JsonCreator
    public ChatMessageResponse(@JsonProperty(TEXT_FIELD) String text,
                               @JsonProperty(CHANNEL_FIELD) String channel) {
        this.text = text;
        this.channel = channel;
    }

    public String getText() {
        return text;
    }

    public String getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "abstractMessage='" + super.toString() + '\'' +
                "text='" + text + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatMessageResponse)) return false;
        ChatMessageResponse that = (ChatMessageResponse) o;
        return Objects.equals(text, that.text) &&
                Objects.equals(channel, that.channel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, channel);
    }
}
