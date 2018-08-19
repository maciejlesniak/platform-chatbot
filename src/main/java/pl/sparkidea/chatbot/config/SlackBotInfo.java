package pl.sparkidea.chatbot.config;

/**
 * @author Maciej Lesniak
 */
public class SlackBotInfo {

    private final String appId;
    private final String verificationToken;
    private String botId;

    public SlackBotInfo(String appId,
                 String verificationToken,
                 String botId) {
        this.appId = appId;
        this.verificationToken = verificationToken;
        this.botId = botId;
    }

    public String getAppId() {
        return appId;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public String getBotId() {
        return botId;
    }

}
