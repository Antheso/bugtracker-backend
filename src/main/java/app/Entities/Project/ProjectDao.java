package app.Entities.Project;

import app.DB.PostgreConnector;
import app.Util.MyLogger;
import org.eclipse.jetty.util.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static app.DB.Query.SELECT_TABLE_PROJECTS;

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
}
