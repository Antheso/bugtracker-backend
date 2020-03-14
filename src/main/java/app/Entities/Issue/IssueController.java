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
        if (data.isEmpty()) {
            throw new Exception("Select issue failed");
        }
        ctx.json(new Response(Response.Status.OK, data));
    };

    public static Handler fetchIssueByID = ctx -> {
        Issue tempIssue = IssueDao.getIssueByID(ctx.pathParam("id"));
        if (tempIssue == null) {
            throw new Exception("Issue not found");
        }
        ctx.json(new Response(Response.Status.OK, tempIssue));
    };

    public static Handler insertIssue = ctx -> {
        ObjectMapper om = new ObjectMapper();
        Issue issue = om.readValue(ctx.body(), Issue.class);

        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(ctx);
        User author = jwt != null ? tokenStorage.get(jwt.getToken()) : null;
        if (author == null) {
            throw new Exception("Insert issue failed");
        }

        int insertRow = IssueDao.addIssue(
                issue.getSummary(),
                issue.getDescription(),
                issue.getPriorityId(),
                issue.getTypeId(),
                issue.getStatusId(),
                issue.getProject().getProjectId(),
                issue.getAssignee().getUserId(),
                author.getUserId()
        );

        if (insertRow <= 0) {
            throw new Exception("Insert issue failed");
        }

        ctx.json(new Response(Response.Status.OK, issue));

        Set<String> receivers = new HashSet<>();
        receivers.add(issue.getAuthor().getEmail());
        receivers.add(issue.getAssignee().getEmail());
        emailNotificator.sendIssueNotification(issue, NotificationType.IssueNotification.NEW_ISSUE, receivers);
    };

    public static Handler updateIssue = ctx -> {
        ObjectMapper om = new ObjectMapper();
        Issue issue = om.readValue(ctx.body(), Issue.class);
        Issue oldIssue = IssueDao.getIssueByID(issue.getId());
        if (oldIssue == null) {
            throw new Exception("Cannot get issue to update!");
        }

        int updateRow = IssueDao.updateIssue(
                ctx.pathParam("id"),
                issue.getSummary(),
                issue.getDescription(),
                issue.getPriorityId(),
                issue.getTypeId(),
                issue.getStatusId(),
                issue.getProject().getProjectId(),
                issue.getAssignee().getUserId()
        );

        if (updateRow <= 0) {
            throw new Exception("Update issue failed");
        }

        ctx.json(new Response(Response.Status.OK, issue));

        Set<String> receivers = new HashSet<>();
        receivers.add(issue.getAuthor().getEmail());
        receivers.add(issue.getAssignee().getEmail());

        // todo: определиться с тем, что еще нотифицировать конкретно
        if (!oldIssue.getStatusId().equals(issue.getStatusId())) {
            emailNotificator.sendIssueNotification(issue,
                    NotificationType.IssueNotification.STATUS_CHANGED,
                    receivers);
        } else if (!oldIssue.getDescription().equals(issue.getDescription())) {
            emailNotificator.sendIssueNotification(issue,
                    NotificationType.IssueNotification.DESCRIPTION_CHANGED,
                    receivers);
        } else {
            emailNotificator.sendIssueNotification(issue,
                    NotificationType.IssueNotification.SOMETHING_CHANGED,
                    receivers);
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
        receivers.add(issue.getAuthor().getEmail());
        receivers.add(issue.getAssignee().getEmail());
        emailNotificator.sendIssueNotification(issue, NotificationType.IssueNotification.ISSUE_DELETED, receivers);
    };
}
