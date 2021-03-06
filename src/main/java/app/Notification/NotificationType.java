package app.Notification;

import app.Entities.Comment.Comment;
import app.Entities.Issue.Issue;
import app.Entities.User.User;
import app.Javalin.JavalinManager;

import java.util.Date;

public class NotificationType {
    public enum UserNotification {
        CONFIRM_EMAIL("") {
            @Override
            public String generateText(User user) {
                return null;
            }
        },

        COMPLETE_REGISTRATION("USER | You have successfully registered!") {
            @Override
            public String generateText(User user) {
                // todo: ссылка для подтверждение регистрации, почты и тд
                String token = JavalinManager.provider.generateToken(user);
                return String.format("Now you have to complete the registration, https://t1.sumdu-tss.site/api/registration/%s",
                        token);
            }
        },

        RESET_PASSWORD("") {
            @Override
            public String generateText(User user) {
                return null;
            }
        };

        private String subject;

        UserNotification(String subject) {
            this.subject = subject;
        }

        public String getSubject() {
            return subject;
        }

        public abstract String generateText(User user);
    }

    public enum IssueNotification {
        STATUS_CHANGED("ISSUE | Status changed!") {
            @Override
            public String generateText(Issue issue) {
                return String.format("Issue %s has changed status to: \"%s\".",
                        issue.getId(),
                        issue.getStatusId());
            }
        },

        DESCRIPTION_CHANGED("ISSUE | Description changed!") {
            @Override
            public String generateText(Issue issue) {
                return String.format("Issue %s has changed description to \n---\n%s\n---\n",
                        issue.getId(),
                        issue.getDescription());
            }
        },

        SOMETHING_CHANGED("ISSUE | Something changed!") {
            @Override
            public String generateText(Issue issue) {
                return String.format("Issue %s has some changes! Please, check it out!",
                        issue.getId());
            }
        },

        NEW_ISSUE("ISSUE | New issue created!") {
            @Override
            public String generateText(Issue issue) {
                return String.format("Issue %s has been created: \n[%s]\n---\n%s\n---\n",
                        issue.getId(),
                        issue.getSummary(),
                        issue.getDescription());
            }
        },

        ISSUE_DELETED("ISSUE | An issue has been deleted!") {
            @Override
            public String generateText(Issue issue) {
                return String.format("Issue %s has been deleted!",
                        issue.getId());
            }
        };

        private String subject;

        IssueNotification(String subject) {
            this.subject = subject;
        }

        public String getSubject() {
            return subject;
        }

        public abstract String generateText(Issue issue);
    }

    public enum CommentNotification {
        NEW_COMMENT("COMMENT | New comment left") {
            @Override
            public String generateText(Comment comment) {
                return String.format("Comment %s was left by %s. \n---\n\"%s\"\n---\n",
                        comment.getCommentId(),
                        comment.getUser(),
                        comment.getText());
            }
        };

        private String subject;

        CommentNotification(String subject) {
            this.subject = subject;
        }

        public String getSubject() {
            return subject;
        }

        public abstract String generateText(Comment comment);
    }
}
