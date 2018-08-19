package pl.sparkidea.chatbot.events.slack.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * URL verification handshake response to Slack server
 *
 * @author Maciej Lesniak
 */
@Getter
@ToString
@EqualsAndHashCode
public class ChallengeResponse {

    public final static String CHALLENGE_FIELD = "challenge";

    @JsonProperty(CHALLENGE_FIELD)
    private String challenge;

    @JsonCreator
    public ChallengeResponse(@JsonProperty(CHALLENGE_FIELD) String challenge) {
        this.challenge = challenge;
    }

}
