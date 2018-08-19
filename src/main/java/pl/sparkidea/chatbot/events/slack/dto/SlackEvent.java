package pl.sparkidea.chatbot.events.slack.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Maciej Lesniak
 */
public class SlackEvent {

    public static final String TOKEN_FIELD = "token";
    public static final String TEAM_ID_FIELD = "team_id";
    public static final String API_APP_ID_FIELD = "api_app_id";
    public static final String TYPE_FIELD = "type";
    public static final String EVENT_ID_FIELD = "event_id";
    public static final String EVENT_TIME_FIELD = "event_time";
    public static final String AUTHED_USERS_FIELD = "authed_users";
    public static final String EVENT_FIELD = "event";

    @JsonProperty(TOKEN_FIELD)
    private final String token;
    @JsonProperty(TEAM_ID_FIELD)
    private final String teamId;
    @JsonProperty(API_APP_ID_FIELD)
    private final String apiAppId;
    @JsonProperty(EVENT_FIELD)
    private final JsonNode eventMessage;
    @JsonProperty(TYPE_FIELD)
    private final String type;
    @JsonProperty(EVENT_ID_FIELD)
    private final String eventId;
    @JsonProperty(EVENT_TIME_FIELD)
    private final Instant eventTime;
    @JsonProperty(AUTHED_USERS_FIELD)
    private final Collection<String> authedUsers;

    @JsonCreator
    public SlackEvent(@JsonProperty(TOKEN_FIELD) String token,
                      @JsonProperty(TEAM_ID_FIELD) String teamId,
                      @JsonProperty(API_APP_ID_FIELD) String apiAppId,
                      @JsonProperty(EVENT_FIELD) JsonNode eventMessage,
                      @JsonProperty(TYPE_FIELD) String type,
                      @JsonProperty(EVENT_ID_FIELD) String eventId,
                      @JsonProperty(EVENT_TIME_FIELD) Instant eventTime,
                      @JsonProperty(AUTHED_USERS_FIELD) Collection<String> authedUsers) {
        this.token = token;
        this.teamId = teamId;
        this.apiAppId = apiAppId;
        this.eventMessage = eventMessage;
        this.type = type;
        this.eventId = eventId;
        this.eventTime = eventTime;
        this.authedUsers = authedUsers;
    }

    public String getToken() {
        return token;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getApiAppId() {
        return apiAppId;
    }

    public JsonNode getEventMessage() {
        return eventMessage;
    }

    public String getType() {
        return type;
    }

    public String getEventId() {
        return eventId;
    }

    public Instant getEventTime() {
        return eventTime;
    }

    public Collection<String> getAuthedUsers() {
        return authedUsers;
    }

    @Override
    public String toString() {
        return "EventMessage{" +
                "token='" + token + '\'' +
                ", teamId='" + teamId + '\'' +
                ", apiAppId='" + apiAppId + '\'' +
                ", eventMessage=" + eventMessage +
                ", type='" + type + '\'' +
                ", eventId='" + eventId + '\'' +
                ", eventTime=" + eventTime +
                ", authedUsers=" + authedUsers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SlackEvent)) return false;
        SlackEvent that = (SlackEvent) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(teamId, that.teamId) &&
                Objects.equals(apiAppId, that.apiAppId) &&
                Objects.equals(eventMessage, that.eventMessage) &&
                Objects.equals(type, that.type) &&
                Objects.equals(eventId, that.eventId) &&
                Objects.equals(eventTime, that.eventTime) &&
                Objects.equals(authedUsers, that.authedUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, teamId, apiAppId, eventMessage, type, eventId, eventTime, authedUsers);
    }
}
