package pl.sparkidea.chatbot.events.slack.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import pl.sparkidea.chatbot.events.slack.dto.ChatMessageResponse;

/**
 * @author Maciej Lesniak
 */
public class ReplyTask implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(ReplyTask.class);
    public static final String POST_CHAT_MESSAGE_ENDPOINT = "/chat.postMessage";

    private final String reply;
    private final String channelId;
    private final WebClient slackWebClient;

    public ReplyTask(String reply,
                     String channelId,
                     WebClient slackWebClient) {
        this.reply = reply;
        this.channelId = channelId;
        this.slackWebClient = slackWebClient;
    }

    @Override
    public void run() {
        slackWebClient.post()
                .uri(POST_CHAT_MESSAGE_ENDPOINT)
                .body(BodyInserters.fromObject(new ChatMessageResponse(reply, channelId)))
                .exchange()
                .subscribe(
                        clientResponse -> LOG.debug("Replied: " + reply),
                        throwable -> LOG.error("Error while replying: ", throwable)
                );
    }

}
