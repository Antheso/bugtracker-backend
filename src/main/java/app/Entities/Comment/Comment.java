package app.Entities.Comment;

import app.Entities.User.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = CommentDeserializer.class)
public class Comment {
    String commentId;
    String text;
    String issueNumber;
    User user;
    long timestamp;

    public Comment(String issueNumber, String commentId, String text, User user, long timestamp) {
        this.issueNumber = issueNumber;
        this.commentId = commentId;
        this.text = text;
        this.user = user;
        this.timestamp = timestamp;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", text='" + text + '\'' +
                ", issueId='" + issueNumber + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
