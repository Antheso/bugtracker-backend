package app.Entities.Comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;

public class CommentController {
    public static Handler fetchAllComment = ctx -> {
        ctx.json(CommentDao.getComments(ctx.pathParam("issueId")));
    };

    public static Handler addComment = ctx -> {
        try{
            ObjectMapper om = new ObjectMapper();

            Comment comment = om.readValue(ctx.body(), Comment.class);

            CommentDao.addComment(
                comment.getText(),
                comment.getUserId(),
                comment.getIssueId()
            );
        }
        catch(Exception ex){
            ex.printStackTrace();
            ctx.result(ex.toString());
        }
    };
}
