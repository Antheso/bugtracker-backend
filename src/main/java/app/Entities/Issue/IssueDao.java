package app.Entities.Issue;

import app.DB.PostgreConnector;
import app.Entities.Project.Project;
import app.Entities.User.User;
import app.Util.MyLogger;
import org.eclipse.jetty.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import static app.DB.Query.*;

public class IssueDao {
    private static MyLogger logger = MyLogger.getLogger(IssueDao.class);

    public static ArrayList<Issue> getTableIssue() throws SQLException {
        ArrayList<Issue> issues = new ArrayList<Issue>();
        Connection connection = PostgreConnector.createConnection();
        try {
            ResultSet resultSet = PostgreConnector.executeSQL(connection, SELECT_TABLE_INFO_ISSUES);

            while (resultSet.next()) {
                String issueId = resultSet.getString("issue_id");
                String summary = resultSet.getString("summary");

                if (!StringUtil.isEmpty(issueId) && !StringUtil.isEmpty(summary))
                    issues.add(new Issue(issueId, summary));
            }
        } catch (SQLException ex) {
            logger.error(ex);
        } finally {
            connection.close();
        }
        return issues;
    }

    public static Issue getIssueByID(String id) throws SQLException {
        ArrayList<Issue> issues = new ArrayList<Issue>();
        Connection connection = PostgreConnector.createConnection();
        try {
            ResultSet resultSet = PostgreConnector.executeSQL(connection, SELECT_ISSUE_BY_ID(id));
            while (resultSet.next()) {
                String statusId = resultSet.getString("status_id");
                String priorityId = resultSet.getString("priority_id");
                String issueId = resultSet.getString("issue_id");
                String summary = resultSet.getString("summary");
                String description = resultSet.getString("description");
                Project project = new Project(resultSet.getString("project_id"), null);
                User assigne = new User(resultSet.getString("assigne_id"), null);
                User author = new User(resultSet.getString("author_id"), null);

                issues.add(new Issue(issueId, summary, description, priorityId, statusId, project, assigne, author));
            }
        } catch (SQLException ex) {
            logger.error(ex);
        } finally {
            connection.close();
        }

        return issues.get(0);
    }

    public static int addIssue(String summary,
                               String description,
                               String priorityId,
                               String statusId,
                               String projectId,
                               String assigneId,
                               String userId
    ) throws SQLException {
        Connection connection = PostgreConnector.createConnection();
        try {
            PostgreConnector.createConnection();
            PreparedStatement statement = PostgreConnector.createStatement(connection, INSERT_ISSUE_PARAMS);
            statement.setString(1, summary);
            statement.setString(2, description);
            statement.setInt(3, Integer.parseInt(priorityId));
            statement.setInt(4, Integer.parseInt(statusId));
            statement.setObject(5, UUID.fromString(projectId));
            statement.setObject(6, UUID.fromString(assigneId));
            statement.setObject(7, UUID.fromString(userId));
            return statement.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
        } finally {
            connection.close();
        }

        return 0;
    }

    public static int updateIssue(String id,
                                  String summary,
                                  String description,
                                  String priorityId,
                                  String statusId,
                                  String projectId,
                                  String assigneId
    ) throws SQLException {
        Connection connection = PostgreConnector.createConnection();
        try {
            PostgreConnector.createConnection();
            PreparedStatement statement = PostgreConnector.createStatement(connection, UPDATE_ISSUE_BY_ID);
            statement.setString(1, summary);
            statement.setString(2, description);
            statement.setInt(3, Integer.parseInt(priorityId));
            statement.setInt(4, Integer.parseInt(statusId));
            statement.setObject(5, UUID.fromString(projectId));
            statement.setObject(6, UUID.fromString(assigneId));
            statement.setObject(7, UUID.fromString(id));

            return statement.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
        } finally {
            connection.close();
        }
        return 0;
    }

    public static int deleteIssue(String id) throws SQLException {
        Connection connection = PostgreConnector.createConnection();
        try {
            PostgreConnector.createConnection();
            return PostgreConnector.createStatement(connection, DELETE_ISSUE_BY_ID(id)).executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
        } finally {
            connection.close();
        }
        return 0;
    }
}
