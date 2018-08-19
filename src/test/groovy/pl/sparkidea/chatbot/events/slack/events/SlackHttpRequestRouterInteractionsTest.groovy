package pl.sparkidea.chatbot.events.slack.events

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.internal.Charsets
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import pl.sparkidea.chatbot.ChatbotApplication
import pl.sparkidea.chatbot.config.SlackWebTestClientConfig
import pl.sparkidea.events.dispatcher.EventHandler
import spock.lang.Specification
import spock.lang.Unroll
import spock.mock.DetachedMockFactory

/**
 *
 * @author Maciej Lesniak
 */
@SpringBootTest(classes = [
        ChatbotApplication.class,
        SlackWebTestClientConfig.class
])
@AutoConfigureWebTestClient
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SlackHttpRequestRouterInteractionsTest extends Specification {

    @Autowired
    private WebTestClient client

    @Autowired
    private ObjectMapper objectMapper

    @Autowired
    private ReactiveWebApplicationContext context


    @Unroll
    def "App should invoke #beanName when handling #jsonMessage"() {

        given:
        def endpoint = '/apis/slack'
        def message = this.getClass().getClassLoader().getResource(jsonMessage).getText(Charsets.UTF_8.name())

        when:
        client.post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(message)
                .exchange()

        then:
        1 * (context.getBean(beanName) as EventHandler<JsonNode, ?>)
                .handle(objectMapper.readTree(message))

        where:
        beanName                 | jsonMessage
        'appMentionedHandler'    | 'app-mentioned-message.json'
        'botMessageHandler'      | 'bot-message.json'
        'callbackMessageHandler' | 'callback-message.json'
        'challengeHandler'       | 'challenge-message.json'
    }

    @TestConfiguration
    static class Config {
        private DetachedMockFactory factory = new DetachedMockFactory()

        @Bean
        AppMentionedHandler appMentionedHandler() {
            factory.Mock(AppMentionedHandler)
        }

        @Bean
        BotMessageHandler botMessageHandler() {
            factory.Mock(BotMessageHandler)
        }

        @Bean
        CallbackMessageHandler callbackMessageHandler() {
            factory.Mock(CallbackMessageHandler)
        }

        @Bean
        ChallengeHandler challengeHandler() {
            factory.Mock(ChallengeHandler)
        }

    }

}
