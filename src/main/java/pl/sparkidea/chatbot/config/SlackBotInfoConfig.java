package pl.sparkidea.chatbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Maciej Lesniak
 */
@Configuration
public class SlackBotInfoConfig {

    @Bean
    public SlackBotInfo info(@Value("${slack.appId}") String appId,
                             @Value("${slack.verificationToken}") String verificationToken,
                             @Value("${slack.botId}") String botId) {

        return new SlackBotInfo(appId, verificationToken, botId);
    }

}
