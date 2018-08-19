package pl.sparkidea.chatbot.events.slack.events

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.internal.Charsets
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import pl.sparkidea.chatbot.ChatbotApplication
import pl.sparkidea.chatbot.config.SlackWebTestClientConfig
import spock.lang.Specification
import spock.lang.Unroll

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

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
class SlackHttpRequestRouterResponsesTest extends Specification {

    @Autowired
    private WebTestClient client

    @Autowired
    private ObjectMapper objectMapper

    @Unroll
    def "App should handle #endpoint message #jsonMessage"() {

        given: "for given json request #jsonMessage to #endpoint"
        def message = this.getClass().getClassLoader().getResource(jsonMessage).getText(Charsets.UTF_8.name())

        expect: "Web Client will consume the message and response with HTTP code #responseCode"
        def post = client.post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(message)
                .exchange()
                .expectStatus().isEqualTo(responseCode)

        and: "Expected body will be #responseBody"
        post.expectBody(JsonNode).consumeWith(
                { EntityExchangeResult<JsonNode> node ->
                    if (responseBody != null) {
                        def expectedJsonNode = objectMapper.readTree(responseBody);
                        assertEquals(expectedJsonNode, node.responseBody)
                    } else {
                        assertTrue(node.responseBody == null)
                    }
                })

        where:
        endpoint      | jsonMessage                  | responseCode | responseBody
        '/apis/slack' | 'app-mentioned-message.json' | 200          | null
        '/apis/slack' | 'bot-message.json'           | 200          | null
        '/apis/slack' | 'callback-message.json'      | 200          | null
        '/apis/slack' | 'challenge-message.json'     | 200          | """{"challenge": "3eZbrw1aBm2rZgRNFdxV2595E9CY3gmdALWMmHkvFXO7tYXAYM8P"}"""
    }

}
