package pl.sparkidea.chatbot

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import pl.sparkidea.chatbot.config.SlackWebTestClientConfig
import spock.lang.Specification

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
class ChatbotApplicationTest extends Specification {

    @Autowired
    private ReactiveWebApplicationContext context

    @Autowired
    private WebTestClient client

    def "Main"() {
        expect: "Application context exists"
        context != null
    }

}
