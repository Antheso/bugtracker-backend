package app.Entities.Issue;

import app.DB.PostgreConnector;
import org.eclipse.jetty.util.StringUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import static app.DB.Query.*;

public class IssueDao {

    public static ArrayList<Issue> issues;

    private static PreparedStatement stm;

    public IssueDao() {
    }

    public static ArrayList<Issue> getTableIssue()
    {
        issues = new ArrayList<Issue>();
        try {
            ResultSet resultSet = PostgreConnector.executeSQL(SELECT_TABLE_INFO_ISSUES);

            while (resultSet.next()) {
                String issueId = resultSet.getString("issue_id");
                String summary = resultSet.getString("summary");

                if(StringUtil.isEmpty(issueId) && StringUtil.isEmpty(summary))
                    continue;

                Issue tempIssue = new Issue();
                tempIssue.setId(issueId);
                tempIssue.setSummary(summary);

                issues.add(tempIssue);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return issues;
    }

    public static Issue getIssueByID(String id)
    {
        issues = new ArrayList<Issue>();
        try
        {
            ResultSet resultSet = PostgreConnector.executeSQL(SELECT_ISSUE_BY_ID(id));
            while (resultSet.next())
            {
                issues.add(new Issue(resultSet));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return issues.get(0);
    }

    public static int addIssue
    (
        String summary,
        String description,
        String priorityId,
        String statusId,
        String projectId,
        String assigneId,
        String userId
    )
    {
        try
        {
            stm = PostgreConnector.createStatement(INSERT_ISSUE_PARAMS);
            stm.setString(1, summary);
            stm.setString(2, description);
            stm.setInt(3, Integer.parseInt(priorityId));
            stm.setInt(4, Integer.parseInt(statusId));
            stm.setObject(5, UUID.fromString(projectId));
            stm.setObject(6, UUID.fromString(assigneId));
            stm.setObject(7, UUID.fromString(userId));
            return stm.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        return 0;
    }

    public static int updateIssue
    (
        String id,
        String summary,
        String description,
        String priorityId,
        String statusId,
        String projectId,
        String assigneId
    )
    {
        try
        {
            stm = PostgreConnector.createStatement(UPDATE_ISSUE_BY_ID);
            stm.setString(1, summary);
            stm.setString(2, description);
            stm.setInt(3, Integer.parseInt(priorityId));
            stm.setInt(4, Integer.parseInt(statusId));
            stm.setObject(5, UUID.fromString(projectId));
            stm.setObject(6, UUID.fromString(assigneId));
            stm.setObject(7, UUID.fromString(id));

            return stm.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        return 0;
    }

    public static int deleteIssue(String id)
    {
        try
        {
            return PostgreConnector.createStatement(DELETE_ISSUE_BY_ID(id)).executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }
}
