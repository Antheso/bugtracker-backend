package app.Entities.Project;

import app.DB.PostgreConnector;
import org.eclipse.jetty.util.StringUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static app.DB.Query.SELECT_TABLE_PROJECTS;

public class ProjectDao {
    public static ArrayList<Project> getProjects()
    {
        ArrayList<Project> projects = new ArrayList<Project>();
        try
        {
            PostgreConnector.createConnection();
            ResultSet resultSet = PostgreConnector.executeSQL(SELECT_TABLE_PROJECTS);

            while (resultSet.next())
            {
                String projectId = resultSet.getString("project_id");
                String name = resultSet.getString("name");

                if(StringUtil.isEmpty(projectId) && StringUtil.isEmpty(name))
                    continue;

                projects.add(new Project(projectId, name));
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
        return projects;
    }
}
