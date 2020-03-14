package app.Entities.Comment;

import app.Entities.User.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = CommentDeserializer.class)
public class Comment {
    String commentId;
    String text;
    String issueId;
    User user;
    long timestamp;

    public Comment(String issueId, String commentId, String text, User user, long timestamp) {
        this.issueId = issueId;
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

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
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
                ", issueId='" + issueId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
