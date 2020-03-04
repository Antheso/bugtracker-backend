package app.Entities.Issue;

import app.DB.PostgreConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static app.DB.Query.*;

public class IssueDao {

    public static ArrayList<Issue> issues;

    public IssueDao() {
    }

    public static ArrayList<Issue> getTableIssue()
    {
        issues = new ArrayList<Issue>();
        try {
            Statement stmt = PostgreConnector.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(SELECT_TABLE_INFO_ISSUES);

            while (resultSet.next()) {
                Issue tempIssue = new Issue();

                String issueId = resultSet.getString("issue_id");
                String number = resultSet.getString("number");
                String name = resultSet.getString("name");

                if(number == null && name == null)// bad way
                    continue;

                tempIssue.setId(issueId);
                tempIssue.setSummary(name);
                //tempIssue.setNumber(number);

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
            Statement stmt = PostgreConnector.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(SELECT_ISSUE_BY_ID(id));

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

    public static void addIssue
            (
                    String summary,
                    String description,
                    String priorityId,
                    String statusId,
                    String projectId,
                    String assigneId
            )
    {
        Statement stmt = null;
        try
        {
            stmt = PostgreConnector.connection.createStatement();
            stmt.executeUpdate(INSERT_ISSUE_PARAMS(summary, description, priorityId, statusId, projectId, assigneId));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public static void updateIssue
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
        Statement stmt = null;
        try
        {
            stmt = PostgreConnector.connection.createStatement();
            stmt.executeUpdate(UPDATE_ISSUE_BY_ID(id, summary, description, priorityId, statusId, projectId, assigneId));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public static void deleteIssue(String id)
    {
        Statement stmt = null;
        ResultSet resultSet = null;
        try
        {
            stmt = PostgreConnector.connection.createStatement();
            stmt.executeUpdate(DELETE_ISSUE_BY_ID(id));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
}
