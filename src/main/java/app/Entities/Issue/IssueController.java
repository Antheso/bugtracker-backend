package app.Entities.Issue;

import app.Entities.User.User;
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

import static app.Javalin.JavalinManager.tokenStorage;

public class IssueController {
    private static EmailNotificator emailNotificator = new EmailNotificator();

    public static Handler fetchAllIssue = ctx -> {
        ArrayList<Issue> data = IssueDao.getTableIssue();
        if (!data.isEmpty())
            ctx.json(new Response(Response.Status.OK, data));
        else {
            throw new Exception("Select issue failed");
        }
    };

    public static Handler fetchIssueByID = ctx -> {
        Issue tempIssue = IssueDao.getIssueByID(ctx.pathParam("id"));
        if (tempIssue != null) {
            ctx.json(new Response(Response.Status.OK, tempIssue));
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
            ctx.json(new Response(Response.Status.OK, issue));
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
            ctx.json(new Response(Response.Status.OK, issue));
        } else {
            throw new Exception("Update issue failed");
        }
    };

    public static Handler deleteIssue = ctx -> {
        Issue issue = IssueDao.getIssueByID(ctx.pathParam("id"));
        if (issue == null) {
            throw new Exception("Cannot fetch issue to delete it!");
        }

        int deleteRow = IssueDao.deleteIssue(ctx.pathParam("id"));
        if (deleteRow <= 0) {
            throw new Exception("Delete issue failed");
        }

        ctx.json(new Response(Response.Status.OK, "delete"));

        Set<String> receivers = new HashSet<>();
        emailNotificator.sendIssueNotification(issue, NotificationType.IssueNotification.ISSUE_DELETED, receivers);
    };
}
