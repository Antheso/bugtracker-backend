package app.Entities.Issue;

import app.Entities.User.User;
import app.Security.JavalinJWT;
import app.Util.Response;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;

import java.util.ArrayList;

import static app.Javalin.JavalinManager.tokenStorage;

public class IssueController {
    public static Handler fetchAllIssue = ctx -> {
        ArrayList<Issue> data = IssueDao.getTableIssue();
        if (!data.isEmpty())
            ctx.json(new Response(true, data));
        else {
            throw new Exception("Select issue failed");
        }
    };

    public static Handler fetchIssueByID = ctx -> {
        Issue tempIssue = IssueDao.getIssueByID(ctx.pathParam("id"));
        if (tempIssue != null) {
            ctx.json(new Response(true, tempIssue));
        } else {
            throw new Exception("Issue not found");
        }
    };

    public static Handler insertIssue = ctx -> {
        ObjectMapper om = new ObjectMapper();
        Issue issue = om.readValue(ctx.body(), Issue.class);

        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(ctx);
        User author = jwt != null ? tokenStorage.get(jwt.getToken()) : null;

        int insertRow = 0;
        if (author != null) {
            insertRow = IssueDao.addIssue(
                    issue.getSummary(),
                    issue.getDescription(),
                    issue.getPriorityId(),
                    issue.getStatusId(),
                    issue.getProject().getProjectId(),
                    issue.getAssignee().getUserId(),
                    author.getUserId()
            );
        } else {
            throw new Exception("Insert issue failed");
        }

        if (insertRow > 0) {
            ctx.json(new Response(true, issue));
        } else {
            throw new Exception("Insert issue failed");
        }
    };

    public static Handler updateIssue = ctx -> {
        ObjectMapper om = new ObjectMapper();
        Issue issue = om.readValue(ctx.body(), Issue.class);
        int updateRow = IssueDao.updateIssue(
                ctx.pathParam("id"),
                issue.getSummary(),
                issue.getDescription(),
                issue.getPriorityId(),
                issue.getStatusId(),
                issue.getProject().getProjectId(),
                issue.getAssignee().getUserId()
        );

        if (updateRow > 0) {
            ctx.json(new Response(true, issue));
        } else {
            throw new Exception("Update issue failed");
        }
    };

    public static Handler deleteIssue = ctx -> {
        int deleteRow = IssueDao.deleteIssue(ctx.pathParam("id"));
        if (deleteRow > 0) {
            ctx.json(new Response(true, "delete"));
        } else {
            throw new Exception("Delete issue failed");
        }
    };
}
