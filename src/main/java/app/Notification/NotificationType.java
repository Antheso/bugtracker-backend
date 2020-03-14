package app.Notification;

import app.Entities.Issue.Issue;

public class NotificationType {
    public enum UserNotification {
        CONFIRM_EMAIL,
        CONFIRM_REGISTRATION,
        RESET_PASSWORD
    }

    public enum IssueNotification {
        STATUS_CHANGED("ISSUE | Status changed!") {
            @Override
            public String getText(Issue issue) {
                return null;
            }
        },

        DESCRIPTION_CHANGED("ISSUE | Description changed!") {
            @Override
            public String getText(Issue issue) {
                return null;
            }
        },

        NEW_ISSUE("ISSUE | New issue created!") {
            @Override
            public String getText(Issue issue) {
                return null;
            }
        },

        ISSUE_DELETED("ISSUE | An issue has been deleted!") {
            @Override
            public String getText(Issue issue) {
                return null;
            }
        }
        ;

        private String subject;

        IssueNotification(String subject) {
            this.subject = subject;
        }

        public String getSubject() {
            return subject;
        }

        public abstract String getText(Issue issue);
    }

    public enum CommentNotification {
        NEW_COMMENT
    }
}
