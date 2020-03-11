package app.Entities.Comment;

import app.Util.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;

import java.util.ArrayList;

public class CommentController {
    public static Handler fetchAllComment = ctx -> {
        ArrayList<Comment> data = CommentDao.getComments(ctx.pathParam("issueId"));
        if (data != null) {
            ctx.json(new Response(true, data));
        } else {
            throw new Exception("Comment not found");
        }
    };

    public static Handler addComment = ctx -> {
        ObjectMapper om = new ObjectMapper();
        Comment comment = om.readValue(ctx.body(), Comment.class);

        int insertRow = CommentDao.addComment(
                comment.getText(),
                comment.getUser().getUserId(),
                comment.getIssueId(),
                comment.getTimestamp()
        );
        if (insertRow > 0)
            ctx.json(new Response(true, comment));
        else {
            throw new Exception("Comment not Added");
        }
    };
}
