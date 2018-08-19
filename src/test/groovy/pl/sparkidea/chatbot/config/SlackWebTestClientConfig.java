package pl.sparkidea.chatbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Maciej Lesniak
 */
@TestConfiguration
public class SlackWebTestClientConfig {

    private static final String SLACK_API_GATEWAY = "https://test.fake-slack.com/api";

    @Value("${slack.apiToken}")
    private String slackApiToken;

    @Bean
    public WebClient slackWebClient() {
        return WebClient
                .builder()
                .baseUrl(SLACK_API_GATEWAY)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + slackApiToken)
                .build();
    }

}
