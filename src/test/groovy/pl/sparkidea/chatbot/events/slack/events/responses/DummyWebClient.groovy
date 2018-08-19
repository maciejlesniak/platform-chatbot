package pl.sparkidea.chatbot.events.slack.events.responses

import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.client.WebClient

/**
 *
 * @author Maciej Lesniak
 */
class DummyWebClient implements WebClient {

    DummyWebClient() {}

    @Override
    RequestHeadersUriSpec<?> get() {
        return null
    }

    @Override
    RequestHeadersUriSpec<?> head() {
        return null
    }

    @Override
    RequestBodyUriSpec post() {
        return null
    }

    @Override
    RequestBodyUriSpec put() {
        return null
    }

    @Override
    RequestBodyUriSpec patch() {
        return null
    }

    @Override
    RequestHeadersUriSpec<?> delete() {
        return null
    }

    @Override
    RequestHeadersUriSpec<?> options() {
        return null
    }

    @Override
    RequestBodyUriSpec method(HttpMethod method) {
        return null
    }

    @Override
    Builder mutate() {
        return null
    }
}
