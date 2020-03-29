package app.Entities.Project;

import app.DB.PostgreConnector;
import app.Entities.ProjectUserRole.ProjectUserRole;
import app.Entities.ProjectUserRole.ProjectUserRoleList;
import app.Entities.User.User;
import app.Util.MyLogger;
import com.fasterxml.uuid.impl.UUIDUtil;
import org.eclipse.jetty.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import static app.DB.Query.*;

public class ProjectDao {
    private static MyLogger logger = MyLogger.getLogger(ProjectDao.class);

    public static ArrayList<Project> getProjects() throws SQLException {
        ArrayList<Project> projects = new ArrayList<Project>();
        Connection connection = PostgreConnector.createConnection();
        try {
            ResultSet resultSet = PostgreConnector.executeSQL(connection, SELECT_TABLE_PROJECTS);

            while (resultSet.next()) {
                String projectId = resultSet.getString("project_id");
                String name = resultSet.getString("name");

                if (!StringUtil.isEmpty(projectId) && !StringUtil.isEmpty(name))
                    projects.add(new Project(projectId, name));
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            connection.close();
        }
        return projects;
    }

    public static int addProject(String projectId,
                                 String projectName,
                                 String createBy,
                                 String description,
                                 boolean isPrivate
    ) throws SQLException {
        Connection connection = PostgreConnector.createConnection();
        try {
            PostgreConnector.createConnection();
            PreparedStatement statement = PostgreConnector.createStatement(connection, INSERT_PROJECT_PARAMS);
            statement.setString(1, projectId);
            statement.setString(2, projectName);
            statement.setObject(3, UUID.fromString(createBy));
            statement.setString(4, description);
            statement.setBoolean(5, isPrivate);


            return statement.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
        } finally {
            connection.close();
        }

        return 0;
    }

    public static int updateProject(String projectId,
                                    String projectName,
                                    String createBy,
                                    String description,
                                    boolean isPrivate
    ) throws SQLException {
        Connection connection = PostgreConnector.createConnection();
        try {
            PostgreConnector.createConnection();
            PreparedStatement statement = PostgreConnector.createStatement(connection, UPDATE_PROJECT_BY_ID);

            statement.setString(1, projectName);
            statement.setString(2, description);
            statement.setBoolean(3, isPrivate);
            statement.setString(4, projectId);

            return statement.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            connection.close();
        }
        return 0;
    }

    public static int deleteProject(String id) throws SQLException {
        Connection connection = PostgreConnector.createConnection();
        try {
            PostgreConnector.createConnection();
            PreparedStatement preparedStatement = PostgreConnector.createStatement(connection, DELETE_PROJECT_BY_ID);
            preparedStatement.setString(1, id);
            return preparedStatement.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            connection.close();
        }
        return 0;
    }

    public static int addProjectUsers(ProjectUserRoleList projectUserRoleList, String projectId) throws SQLException {
        Connection connection = PostgreConnector.createConnection();
        try {
            PostgreConnector.createConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = PostgreConnector.createStatement(connection, INSERT_PROJECT_USER_PARAMS);
            int i = 0;
            for (ProjectUserRole projectUser : projectUserRoleList) {
                statement.setString(1, projectId);
                statement.setObject(2, UUIDUtil.uuid(projectUser.getUserId()));
                statement.setInt(3, Integer.parseInt(projectUser.getProjectRoleId()));

                statement.addBatch();
                i++;
                if (i % 1000 == 0 || i == projectUserRoleList.size()) {
                    statement.executeBatch(); // Execute every 1000 items.
                }
            }
            connection.commit();

            return 1;
        } catch (SQLException ex) {
            logger.error(ex);
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }

        return 0;
    }

    public static int updateProjectUsers(ArrayList<ProjectUserRole> updateUsers, String projectId) throws SQLException {
        Connection connection = PostgreConnector.createConnection();
        try {
            PostgreConnector.createConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = PostgreConnector.createStatement(connection, UPDATE_PROJECT_USER_ROLE);
            int i = 0;
            for (ProjectUserRole projectUser : updateUsers) {

                statement.setInt(1, Integer.parseInt(projectUser.getProjectRoleId()));
                statement.setString(2, projectId);
                statement.setObject(3, UUIDUtil.uuid(projectUser.getUserId()));

                statement.addBatch();
                i++;
                if (i % 1000 == 0 || i == updateUsers.size()) {
                    statement.executeBatch(); // Execute every 1000 items.
                }
            }

            connection.commit();

            return 1;
        } catch (SQLException ex) {
            logger.error(ex);
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }

        return 0;
    }

    public static int deleteProjectUsers(ProjectUserRoleList projectUserRoleList, String projectId) throws SQLException {
        Connection connection = PostgreConnector.createConnection();
        try {
            PostgreConnector.createConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = PostgreConnector.createStatement(connection, DELETE_PROJECT_USER);
            int i = 0;
            for (ProjectUserRole projectUser : projectUserRoleList) {

                statement.setString(1, projectId);
                statement.setObject(2, UUIDUtil.uuid(projectUser.getUserId()));

                statement.addBatch();
                i++;
                if (i % 1000 == 0 || i == projectUserRoleList.size()) {
                    statement.executeBatch(); // Execute every 1000 items.
                }
            }

            connection.commit();

            return 1;
        } catch (SQLException ex) {
            logger.error(ex);
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }

        return 0;
    }

//    public static ArrayList<ProjectUserRole> getProjectUser(String projectId) throws SQLException {
//        ArrayList<ProjectUserRole> projects = new ArrayList<ProjectUserRole>();
//        Connection connection = PostgreConnector.createConnection();
//        try {
//            PreparedStatement preparedStatement = PostgreConnector.createStatement(connection, SELECT_TABLE_PROJECT_USER_ROLE);
//            preparedStatement.setString(1, projectId);
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                String projectID = resultSet.getString("project_id");
//                String userId = resultSet.getString("user_id");
//                String projectRoleId = resultSet.getString("project_role_id");
//
//                if (!StringUtil.isEmpty(projectID) && !StringUtil.isEmpty(userId) && !StringUtil.isEmpty(projectRoleId))
//                    projects.add(new ProjectUserRole(projectID, userId, projectRoleId));
//            }
//        } catch (SQLException e) {
//            logger.error(e);
//        } finally {
//            connection.close();
//        }
//        return projects;
//    }

    public static ArrayList<Project> getProjectsByUser(String userId) throws SQLException {
        ArrayList<Project> projects = new ArrayList<Project>();
        Connection connection = PostgreConnector.createConnection();
        try {
            PreparedStatement preparedStatement = PostgreConnector.createStatement(connection, SELECT_USER_PROJECTS);
            preparedStatement.setObject(1, UUIDUtil.uuid(userId));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String projectId = resultSet.getString("project_id");
                String projectName = resultSet.getString("name");
                String description = resultSet.getString("description");
                boolean isPrivate = Boolean.parseBoolean(resultSet.getString("private"));
                String projectRoleId = resultSet.getString("project_role_id");

                if (!StringUtil.isEmpty(projectId) &&
                        !StringUtil.isEmpty(projectName) &&
                        !StringUtil.isEmpty(description) &&
                        !StringUtil.isEmpty(projectRoleId)
                )
                    projects.add(new Project(projectId, projectName, description, isPrivate, projectRoleId));
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            connection.close();
        }
        return projects;
    }


    public static Project getProjectByProjectId(String projectId) throws SQLException {
        ArrayList<Project> projects = new ArrayList<Project>();
        Connection connection = PostgreConnector.createConnection();
        try {
            PreparedStatement preparedStatement = PostgreConnector.createStatement(connection, SELECT_PROJECT_BY_PROJECT_ID);
            preparedStatement.setString(1, projectId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String projectID = resultSet.getString("project_id");
                String projectName = resultSet.getString("name");
                String description = resultSet.getString("description");
                boolean isPrivate = Boolean.parseBoolean(resultSet.getString("private"));

                if (!StringUtil.isEmpty(projectID) && !StringUtil.isEmpty(projectName))
                    projects.add(new Project(projectId, projectName, description, isPrivate, null));
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            connection.close();
        }
        return projects.isEmpty() ? null : projects.get(0);
    }

    public static ProjectUserRoleList getProjectUserRoleByProjectId(String projectId) throws SQLException {
        Connection connection = PostgreConnector.createConnection();
        ProjectUserRoleList projectUserRoleList = new ProjectUserRoleList();
        try {
            PostgreConnector.createConnection();

            PreparedStatement statement = PostgreConnector.createStatement(connection, SELECT_PROJECT_PROJECT_USER_ROLE_PARAMS);
            statement.setString(1, projectId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String userId = resultSet.getString("user_id");
                String projectRoleId = resultSet.getString("project_role_id");

                if (!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(projectRoleId))
                    projectUserRoleList.add(new ProjectUserRole(projectRoleId, userId));
            }

        } catch (SQLException ex) {
            logger.error(ex);
        } finally {
            connection.close();
        }
        return projectUserRoleList;
    };

    public static ArrayList<User> getProjectUsersByProjectId(String projectId, String currentUserId) throws SQLException {
        Connection connection = PostgreConnector.createConnection();
        ArrayList<User> projectUsers = new ArrayList<User>();
        try {
            PostgreConnector.createConnection();

            PreparedStatement statement = PostgreConnector.createStatement(connection, SELECT_PROJECT_USERS_PARAMS);
            statement.setString(1, projectId);
//            statement.setObject(2, UUIDUtil.uuid(currentUserId));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String userId = resultSet.getString("user_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String projectRoleId = resultSet.getString("project_role_id");

                if (!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(projectRoleId))
                    projectUsers.add(new User(firstName, lastName, userId, email, projectRoleId, null));
            }

        } catch (SQLException ex) {
            logger.error(ex);
        } finally {
            connection.close();
        }
        return projectUsers;
    };
}
