package app.Entities.Issue;

import app.Entities.Comment.CommentDao;
import app.Entities.User.User;
import app.Entities.User.UserDao;
import app.Exception.AuthorizationException;
import app.Notification.Email.EmailNotificator;
import app.Notification.NotificationType;
import app.Security.JavalinJWT;
import app.Util.Response;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;

import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class IssueController {

    private static EmailNotificator emailNotificator = new EmailNotificator();

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

        ctx.json(new Response(Response.Status.OK, data));
    };

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
        User author = UserDao.getValidUser(jwt);

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

        if (insertRow <= 0) {
            throw new Exception("Insert issue failed");
        }

        ctx.json(new Response(Response.Status.OK, issue));
        try {
            ArrayList<String> receivers = new ArrayList();
            receivers.add(author.getEmail());
            emailNotificator.sendIssueNotification(issue, NotificationType.IssueNotification.NEW_ISSUE, receivers);
        }catch (Exception ex){

        }
    };

    public static Handler updateIssue = ctx -> {
        ObjectMapper om = new ObjectMapper();
        Issue issue = om.readValue(ctx.body(), Issue.class);
        String newId = issue.project.getProjectId() + "-" + issue.number;
        if (!issue.id.isEmpty() && !issue.id.equals(newId)) {
            replaceIssue(issue);
            ctx.json(new Response(Response.Status.OK, issue));
            return;
        }

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
                issue.getAssignee().getUserId()
        );

        if (updateRow <= 0) {
            throw new Exception("Update issue failed");
        }

        ctx.json(new Response(Response.Status.OK, issue));
        try {
            ArrayList<String> receivers = new ArrayList<String>();
            receivers.add(issue.getAuthor().getEmail());
            receivers.add(issue.getAssignee().getEmail());

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
        }
        catch (Exception ex){

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

        try {
            ArrayList<String> receivers = new ArrayList<String>();
            receivers.add(issue.getAuthor().getEmail());
            receivers.add(issue.getAssignee().getEmail());
            emailNotificator.sendIssueNotification(issue, NotificationType.IssueNotification.ISSUE_DELETED, receivers);
        }catch (Exception ex){

        }
    };

    private static int replaceIssue(Issue issue) throws SQLException {

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
