package app.Notification.Email;

import app.Util.MyLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class EmailNotificator {
    private static final MyLogger log = MyLogger.getLogger(EmailNotificator.class);

    private String username = "";
    private String password = "";

    private EmailSender emailSender;

    public EmailNotificator() {
        readProperties();
        emailSender = new EmailSender(username, password);
    }

    private void readProperties() {
        Properties emailProperties = new Properties();
        String propertiesFilename = "properties/email.properties";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFilename)) {
            if (Objects.nonNull(inputStream)) {
                emailProperties.load(inputStream);
                username = emailProperties.getProperty("username");
                password = emailProperties.getProperty("password");
            }
        } catch (IOException e) {
            log.error(e);
        }
    }

    private String readEmailTextTemplate(String filename) {
        return "";
    }

    public void notifyEmailConfirmation(String receiverEmail, String url) {
        Email email = new Email("", "", "", receiverEmail);
        emailSender.send(email);
    }

    public void notifyCompleteRegistration(String receiverEmail, String url) {
        Email email = new Email("", "", "", receiverEmail);
        emailSender.send(email);
    }

    public void notifyPasswordReset(String receiverEmail, String url) {
        Email email = new Email("", "", "", receiverEmail);
        emailSender.send(email);
    }

    public void notifyTicketChanged(String receiverEmail, String url) {
        throw new AssertionError("Not yet implemented. (Stage 3)");
    }
}
