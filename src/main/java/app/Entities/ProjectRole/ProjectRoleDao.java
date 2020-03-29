package app.Entities.ProjectRole;

import app.DB.PostgreConnector;
import app.Entities.ProjectRole.ProjectRole;
import app.Entities.ProjectRole.ProjectRoleDao;
import app.Util.MyLogger;
import org.eclipse.jetty.util.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static app.DB.Query.SELECT_TABLE_PROJECT_ROLE;


public class ProjectRoleDao {
    private static MyLogger logger = MyLogger.getLogger(ProjectRoleDao.class);

    public static ArrayList<ProjectRole> getProjectRole() throws SQLException {
        ArrayList<ProjectRole> projectRoles = new ArrayList<ProjectRole>();
        Connection connection = PostgreConnector.createConnection();
        try {
            ResultSet resultSet = PostgreConnector.executeSQL(connection, SELECT_TABLE_PROJECT_ROLE);

            while (resultSet.next()) {
                String projectRoleId = resultSet.getString("project_role_id");
                String name = resultSet.getString("name");

                if (!StringUtil.isEmpty(projectRoleId) && !StringUtil.isEmpty(name))
                    projectRoles.add(new ProjectRole(projectRoleId, name));
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            connection.close();
        }
        return projectRoles;
    }
}
