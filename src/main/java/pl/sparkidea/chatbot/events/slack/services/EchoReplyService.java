package pl.sparkidea.chatbot.events.slack.services;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import pl.sparkidea.chatbot.events.slack.dto.AppMentionedEventMessage;
import pl.sparkidea.chatbot.events.slack.dto.CallbackEventMessage;
import pl.sparkidea.chatbot.events.slack.dto.ChanelledMessage;
import pl.sparkidea.chatbot.events.slack.tasks.ReplyTask;

/**
 * @author Maciej Lesniak
 */
@Service
public class EchoReplyService {

    private static final String REPLY_TEMPLATE = "<@%s> Auto reply for mentioning me: %s";

    private final TaskExecutor taskExecutor;
    private final WebClient slackWebClient;

    public EchoReplyService(TaskExecutor taskExecutor,
                            WebClient slackWebClient) {
        this.taskExecutor = taskExecutor;
        this.slackWebClient = slackWebClient;
    }


    public void replyToMessage(AppMentionedEventMessage message) {
        executeReply(message, String.format(REPLY_TEMPLATE, message.getUser(), message.getText()));
    }

    public void replyToMessage(CallbackEventMessage message) {
        executeReply(message, String.format(REPLY_TEMPLATE, message.getUser(), message.getText()));
    }

    private void executeReply(ChanelledMessage message, String replyText) {
        taskExecutor.execute(new ReplyTask(replyText, message.getChannelId(), slackWebClient));
    }

}
