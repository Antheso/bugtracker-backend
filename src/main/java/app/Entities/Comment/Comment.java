package app.Entities.Comment;

import app.Entities.Issue.IssueDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = CommentDeserializer.class)
public class Comment {

    String commentId;
    String text;
    String issueId;
    String userId;
    String timestamp; //DATE - Milesec

    public Comment() {
    }

    public Comment(String issueId, String text, String userId) {
        this.issueId = issueId;
        this.text = text;
        this.userId = userId;
    }

    public Comment(String issueId, String text, String userId, String timestamp) {
        this.issueId = issueId;
        this.text = text;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
