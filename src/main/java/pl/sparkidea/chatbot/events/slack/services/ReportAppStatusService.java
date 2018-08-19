package pl.sparkidea.chatbot.events.slack.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import pl.sparkidea.chatbot.events.slack.tasks.ReplyTask;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

/**
 * @author Maciej Lesniak
 */
@Component
public class ReportAppStatusService {
    private static final String UNKNOWN_VALUE = "_UNKNOWN_";
    private static final String REFRESHED_TEMPLATE = "%s [version: %s] has been REFRESHED at %s on server: %s";

    private WebClient slackWebClient;
    private String appName;
    private String appVersion;
    private String defaultChannelId;

    public ReportAppStatusService(WebClient slackWebClient,
                                  @Value("${spring.application.name}") String appName,
                                  @Value("${spring.application.version}") String appVersion,
                                  @Value("${slack.defaultChannelId}") String defaultChannelId) {
        this.slackWebClient = slackWebClient;
        this.appName = appName;
        this.appVersion = appVersion;
        this.defaultChannelId = defaultChannelId;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void contextRefreshedEvent() {
        new ReplyTask(
                String.format(
                        REFRESHED_TEMPLATE,
                        appName,
                        appVersion,
                        LocalDateTime.now(),
                        getServerInfo()
                ),
                defaultChannelId,
                slackWebClient).run();
    }


    private String getServerInfo() {
        String hostName = UNKNOWN_VALUE;
        String ipAddress = UNKNOWN_VALUE;

        try {
            hostName = InetAddress.getLocalHost().getHostName();
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return String.format("%s [%s]", hostName, ipAddress);
    }


}
