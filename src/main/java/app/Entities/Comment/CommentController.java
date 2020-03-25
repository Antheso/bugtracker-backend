package app.Entities.Comment;

import app.Entities.Issue.Issue;
import app.Entities.Issue.IssueDao;
import app.Entities.User.User;
import app.Entities.User.UserDao;
import app.Notification.Email.EmailNotificator;
import app.Notification.NotificationType;
import app.Security.JavalinJWT;
import app.Util.Response;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CommentController {
    private static EmailNotificator emailNotificator = new EmailNotificator();

    public static Handler fetchAllComment = ctx -> {
        ArrayList<Comment> data = CommentDao.getComments(ctx.pathParam("issueId"));
        ctx.json(new Response(Response.Status.OK, data));
    };

    public static Handler addComment = ctx -> {
        ObjectMapper om = new ObjectMapper();
        Comment comment = om.readValue(ctx.body(), Comment.class);

        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(ctx);
        User author = UserDao.getValidUser(jwt);

        int insertRow = CommentDao.addComment(
                comment.getText(),
                author.getUserId(),
                comment.getIssueNumber(),
                comment.getTimestamp()
        );
        if (insertRow <= 0) {
            throw new Exception("Comment not Added");
        }

        ctx.json(new Response(Response.Status.OK, comment));

        String issueId = comment.getIssueNumber();
        Issue commentedIssue = IssueDao.getIssueByID(issueId);
        if (commentedIssue == null) {
            throw new Exception("Cannot get commented issue by id!");
        }

//        Set<String> receivers = new HashSet<>();
//        receivers.add(comment.getUser().getEmail());
//        receivers.add(commentedIssue.getAuthor().getEmail());
//        receivers.add(commentedIssue.getAssignee().getEmail());
//
//        emailNotificator.sendCommentNotification(comment, NotificationType.CommentNotification.NEW_COMMENT, receivers);
    };
}
