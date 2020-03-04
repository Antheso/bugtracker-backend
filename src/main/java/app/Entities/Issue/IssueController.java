package app.Entities.Issue;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;

import java.util.ArrayList;

public class IssueController {

    public static Handler fetchAllIssue = ctx -> {
        try{
            ArrayList<Issue> d = IssueDao.getTableIssue();
            if (d.size() > 0)        ctx.json(d);
        }
            catch (Exception ex){
                ctx.result(ex.toString());
        }
    };

    public static Handler fetchIssueByID = ctx -> {
        ctx.json(IssueDao.getIssueByID(ctx.pathParam("id")));
    };

    public static Handler insertIssue = ctx -> {
        try{
            ObjectMapper om = new ObjectMapper();

            Issue issue = om.readValue(ctx.body(), Issue.class);

            IssueDao.addIssue(
                    issue.getSummary(),
                    issue.getDescription(),
                    issue.getPriorityId(),
                    issue.getStatusId(),
                    issue.getProject().getProjectId(),
                    issue.getAssignee().getUserId()
            );
        }
        catch(Exception ex){
            ex.printStackTrace();
            ctx.result(ex.toString());
        }
    };

    public  static Handler updateIssue = ctx -> {
        ObjectMapper om = new ObjectMapper();

        Issue issue = om.readValue(ctx.body(), Issue.class);
        try{
            IssueDao.updateIssue(
                    ctx.pathParam("id"),
                    issue.getSummary(),
                    issue.getDescription(),
                    issue.getPriorityId(),
                    issue.getStatusId(),
                    issue.getProject().getProjectId(),
                    issue.getAssignee().getUserId()
            );
        } catch (Exception ex){
            ctx.result(ex.toString());
        }
    };

    public static Handler deleteIssue = ctx -> {
        IssueDao.deleteIssue(ctx.pathParam("id"));
    };
}
