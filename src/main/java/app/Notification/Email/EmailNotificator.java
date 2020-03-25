package app.Notification.Email;

import app.Entities.Comment.Comment;
import app.Entities.Issue.Issue;
import app.Entities.User.User;
import app.Notification.NotificationType;
import app.Util.MyLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

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

    public void sendUserNotification(User user,
                                     NotificationType.UserNotification userNotificationType,
                                     String receiver) {
        emailSender.send(new Email(userNotificationType.getSubject(),
                userNotificationType.generateText(user), username, receiver));
    }

    public void sendIssueNotification(Issue issue,
                                      NotificationType.IssueNotification issueNotificationType,
                                      Set<String> receivers) {
        Email email = new Email(issueNotificationType.getSubject(),
                issueNotificationType.generateText(issue), username, "");
        for (String receiver : receivers) {
            email.setReceiverEmail(receiver);
            emailSender.send(email);
        }
    }

    public void sendCommentNotification(Comment comment,
                                        NotificationType.CommentNotification commentNotificationType,
                                        Set<String> receivers) {
        Email email = new Email(commentNotificationType.getSubject(),
                commentNotificationType.generateText(comment), username, "");
        for (String receiver : receivers) {
            email.setReceiverEmail(receiver);
            emailSender.send(email);
        }
    }
}
