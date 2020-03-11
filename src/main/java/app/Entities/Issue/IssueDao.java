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
            String d = SELECT_TABLE_INFO_ISSUES;
            ResultSet resultSet = PostgreConnector.executeSQL(connection, d);

            while (resultSet.next()) {
                String issueId = resultSet.getString("issue_id");
                String summary = resultSet.getString("summary");
                String number = resultSet.getString("number");

                if (!StringUtil.isEmpty(issueId) && !StringUtil.isEmpty(summary) &&
                    !StringUtil.isEmpty(number)){
                    issues.add(new Issue(issueId, summary, number));
                }
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
            String d = SELECT_ISSUE_BY_ID(id);
            ResultSet resultSet = PostgreConnector.executeSQL(connection, d);
            while (resultSet.next()) {
                String statusId = resultSet.getString("status_id");
                String typeId = resultSet.getString("type_id");
                String priorityId = resultSet.getString("priority_id");
                String issueId = resultSet.getString("issue_id");
                String summary = resultSet.getString("summary");
                String number = resultSet.getString("number");
                String description = resultSet.getString("description");

                String project_id = resultSet.getString("project_id");
                String project_name = resultSet.getString("project_name");

                Project project = new Project(project_id, project_name);
                User assignee = new User(resultSet.getString("assignee_id"), resultSet.getString("assignee_name"));
                User author = new User(resultSet.getString("author_id"), resultSet.getString("author_name"));

                issues.add(new Issue(issueId, summary, description, priorityId, typeId, statusId, project_id + "-" + number, project, assignee, author));
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
                               String typeId,
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
            statement.setInt(5, Integer.parseInt(typeId));
            statement.setObject(6, projectId);
            statement.setObject(7, UUID.fromString(assigneId));
            statement.setObject(8, UUID.fromString(userId));
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
                                  String typeId,
                                  String statusId,
                                  String projectId,
                                  String assigneeId
    ) throws SQLException {
        Connection connection = PostgreConnector.createConnection();
        try {
            PostgreConnector.createConnection();
            PreparedStatement statement = PostgreConnector.createStatement(connection, UPDATE_ISSUE_BY_ID);

            String[] parts = id.split("-");

            statement.setString(1, summary);
            statement.setString(2, description);
            statement.setInt(3, Integer.parseInt(priorityId));
            statement.setInt(4, Integer.parseInt(typeId));
            statement.setInt(5, Integer.parseInt(statusId));
            statement.setString(6, projectId);
            statement.setObject(7, UUID.fromString(assigneeId));
            statement.setString(8, parts[0]);
            statement.setLong(9, Long.parseLong(parts[1]));

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
