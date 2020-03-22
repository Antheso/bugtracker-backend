package app.Entities.Issue;

import app.Entities.Comment.CommentDao;
import app.Entities.User.User;
import app.Exception.AuthorizationException;
import app.Security.JavalinJWT;
import app.Util.Response;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;

import java.sql.SQLException;
import java.util.ArrayList;

import static app.Javalin.JavalinManager.tokenStorage;

public class IssueController {

    public static Handler fetchFilteredIssue = ctx -> {
        ArrayList<Issue> data;
        if (ctx.queryParam("limit").isEmpty() && ctx.queryParam("offset").isEmpty()) {
            throw new Exception("Select issue failed, not limit/offset");
        }

        final int offset = Integer.parseInt(ctx.queryParam("offset"));
        final int limit = Integer.parseInt(ctx.queryParam("limit"));
        final String searchQuery = ctx.queryParam("searchQuery");

        if (searchQuery != null && !searchQuery.isEmpty()) {
            data = IssueDao.getTableIssueSearch(limit, offset, searchQuery);
        } else {
            data = IssueDao.getTableIssueLimit(limit, offset);
        }

        ctx.json(new Response(true, data));
    };

    public static Handler fetchIssueByID = ctx -> {
        Issue tempIssue = IssueDao.getIssueByID(ctx.pathParam("id"));
        if (tempIssue == null) {
            throw new Exception("Issue not found");
        }
        ctx.json(new Response(true, tempIssue));
    };

    public static Handler insertIssue = ctx -> {
        ObjectMapper om = new ObjectMapper();
        Issue issue = om.readValue(ctx.body(), Issue.class);

        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(ctx);
        User author = jwt != null ? tokenStorage.get(jwt.getToken()) : null;

        if (author == null) {
            throw new AuthorizationException("Insert bad author failed");
        }

        int insertRow = IssueDao.addIssue(
                issue.getSummary(),
                issue.getDescription(),
                issue.getPriorityId(),
                issue.getTypeId(),
                issue.getStatusId(),
                issue.getProject().getProjectId(),
                issue.getAssignee().getUserId(),
                author.getUserId(),
                issue.deprecated
        );

        if (insertRow > 0) {
            ctx.json(new Response(true, issue));
        } else {
            throw new Exception("Insert issue failed");
        }
    };

    public static Handler updateIssue = ctx -> {
        ObjectMapper om = new ObjectMapper();

        Issue issue = om.readValue(ctx.body(), Issue.class);
        String newId = issue.project.getProjectId() + "-" + issue.number;
        if (!issue.id.isEmpty() && !issue.id.equals(newId)) {
            replaceIssue(issue, newId);
            ctx.json(new Response(true, issue));
            return;
        }

        int updateRow = IssueDao.updateIssue(
                ctx.pathParam("id"),
                issue.getSummary(),
                issue.getDescription(),
                issue.getPriorityId(),
                issue.getTypeId(),
                issue.getStatusId(),
                issue.getAssignee().getUserId()
        );

        if (updateRow > 0)
            ctx.json(new Response(true, issue));
        else {
            throw new Exception("Update issue failed");
        }
    };

    public static Handler deleteIssue = ctx -> {
        CommentDao.removeAllCommentId(ctx.pathParam("id"));
        int deleteRow = IssueDao.deleteIssue(ctx.pathParam("id"));
        if (deleteRow > 0) {
            ctx.json(new Response(true, "delete"));
        } else {
            throw new Exception("Delete issue failed");
        }
    };

    private static int replaceIssue(Issue issue, String newId) throws SQLException {

        int insertRow = IssueDao.addIssue(
                issue.getSummary(),
                issue.getDescription(),
                issue.getPriorityId(),
                issue.getTypeId(),
                issue.getStatusId(),
                issue.getProject().getProjectId(),
                issue.getAssignee().getUserId(),
                issue.getAuthor().getUserId(),
                issue.id
        );

        CommentDao.updateCommentNumber(issue);
        int deleteRow = IssueDao.deleteIssue(issue.id);

        if(deleteRow > 0 && insertRow > 0) {
            return 1;
        }
        return 0;
    }

}
