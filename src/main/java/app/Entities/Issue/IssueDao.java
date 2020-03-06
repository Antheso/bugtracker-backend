package app.Entities.Issue;

import app.DB.PostgreConnector;
import org.eclipse.jetty.util.StringUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import static app.DB.Query.*;

public class IssueDao {
    public static ArrayList<Issue> getTableIssue()
    {
        ArrayList<Issue> issues = new ArrayList<Issue>();
        try
        {
            PostgreConnector.createConnection();
            ResultSet resultSet = PostgreConnector.executeSQL(SELECT_TABLE_INFO_ISSUES);

            while (resultSet.next())
            {
                String issueId = resultSet.getString("issue_id");
                String summary = resultSet.getString("summary");

                if(StringUtil.isEmpty(issueId) && StringUtil.isEmpty(summary))
                    continue;

                issues.add(new Issue(issueId, summary));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                PostgreConnector.endConnection();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return issues;
    }

    public static Issue getIssueByID(String id)
    {
        ArrayList<Issue> issues = new ArrayList<Issue>();
        try
        {
            PostgreConnector.createConnection();
            ResultSet resultSet = PostgreConnector.executeSQL(SELECT_ISSUE_BY_ID(id));
            while (resultSet.next())
            {
                issues.add(new Issue(resultSet));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                PostgreConnector.endConnection();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
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
            PostgreConnector.createConnection();
            PreparedStatement statement = PostgreConnector.createStatement(INSERT_ISSUE_PARAMS);
            statement.setString(1, summary);
            statement.setString(2, description);
            statement.setInt(3, Integer.parseInt(priorityId));
            statement.setInt(4, Integer.parseInt(statusId));
            statement.setObject(5, UUID.fromString(projectId));
            statement.setObject(6, UUID.fromString(assigneId));
            statement.setObject(7, UUID.fromString(userId));
            return statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                PostgreConnector.endConnection();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
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
            PostgreConnector.createConnection();
            PreparedStatement statement = PostgreConnector.createStatement(UPDATE_ISSUE_BY_ID);
            statement.setString(1, summary);
            statement.setString(2, description);
            statement.setInt(3, Integer.parseInt(priorityId));
            statement.setInt(4, Integer.parseInt(statusId));
            statement.setObject(5, UUID.fromString(projectId));
            statement.setObject(6, UUID.fromString(assigneId));
            statement.setObject(7, UUID.fromString(id));

            return statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                PostgreConnector.endConnection();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        return 0;
    }

    public static int deleteIssue(String id)
    {
        try
        {
            PostgreConnector.createConnection();
            return PostgreConnector.createStatement(DELETE_ISSUE_BY_ID(id)).executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                PostgreConnector.endConnection();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
