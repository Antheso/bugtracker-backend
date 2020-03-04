package app.Entities.Project;

import app.DB.PostgreConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static app.DB.Query.SELECT_TABLE_PROJECTS;


public class ProjectDao {

    public static ArrayList<Project> projects;

    public static ArrayList<Project> getProjects()
    {
        projects = new ArrayList<Project>();
        try {
            Statement stmt = PostgreConnector.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(SELECT_TABLE_PROJECTS);

            while (resultSet.next()) {
                Project tempProject = new Project();

                String projectId = resultSet.getString("project_id");
                String name = resultSet.getString("name");

                if(projectId == null && name == null)// bad way
                    continue;

                tempProject.setProjectId(projectId);
                tempProject.setProjectName(name);

                projects.add(tempProject);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }
}
