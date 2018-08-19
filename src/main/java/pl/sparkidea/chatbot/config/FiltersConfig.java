package pl.sparkidea.chatbot.config;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.sparkidea.chatbot.events.slack.dto.SlackEvent;
import pl.sparkidea.chatbot.events.slack.validators.MessageCredentialsValidator;
import pl.sparkidea.events.filters.AlreadyProcessedJsonEventsFilter;
import pl.sparkidea.events.filters.MonoJsonValidationFilter;
import pl.sparkidea.events.predicates.JsonFieldPredicate;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

/**
 * @author Maciej Lesniak
 */
@Configuration
public class FiltersConfig {

    private final static ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>();
    private final static int MAX_CACHE_SIZE = 100;

    @Bean
    public AlreadyProcessedJsonEventsFilter alreadyProcessedJsonEventsFilter() {
        return new AlreadyProcessedJsonEventsFilter(cache, MAX_CACHE_SIZE, ev -> Optional.ofNullable(ev.get(SlackEvent.EVENT_ID_FIELD)).map(JsonNode::asText));
    }

    @Bean
    public MonoJsonValidationFilter messageCredentialsValidator(SlackBotInfo slackBotInfo) {
        final Predicate<JsonNode> credentialsValidator = new MessageCredentialsValidator(slackBotInfo.getAppId(), slackBotInfo.getVerificationToken());
        final Predicate<JsonNode> isExcludedFromCredentialsValidationValidator = new JsonFieldPredicate("challenge", JsonFieldPredicate.ANY_VALUE);

        return new MonoJsonValidationFilter(credentialsValidator.or(isExcludedFromCredentialsValidationValidator), "Invalid credentials");
    }

}
