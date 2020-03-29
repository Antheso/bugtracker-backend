package app.Notification.Email;

import app.Entities.Comment.Comment;
import app.Entities.Issue.Issue;
import app.Entities.User.User;
import app.Notification.NotificationType;
import app.Util.Configuration;
import app.Util.MyLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

public class EmailNotificator {
    private static final MyLogger log = MyLogger.getLogger(EmailNotificator.class);

    private String username;
    private String password;

    private EmailSender emailSender;

    public EmailNotificator() {
        username = Configuration.properties.getProperty("password");
        password = Configuration.properties.getProperty("username").trim();
        emailSender = new EmailSender(username, password);
    }

    public void sendUserNotification(User user,
                                     NotificationType.UserNotification userNotificationType,
                                     String receiver) {
        try{
            emailSender.send(new Email(userNotificationType.getSubject(),
                    userNotificationType.generateText(user), username, receiver));
        }catch (Exception ex){

        }

    }

    public void sendIssueNotification(Issue issue,
                                      NotificationType.IssueNotification issueNotificationType,
                                      ArrayList<String> receivers) {
        Email email = new Email(issueNotificationType.getSubject(),
                issueNotificationType.generateText(issue), username, "");
        for (int i = 0; i < receivers.size(); i++) {
            email.setReceiverEmail(receivers.get(i));
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
