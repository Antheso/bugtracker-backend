package app.Entities.Priority;

import app.DB.PostgreConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static app.DB.Query.SELECT_TABLE_PRIORITY;

public class PriorityDao {
    public static ArrayList<Priority> priority;

    public static ArrayList<Priority> getPriority()
    {
        priority = new ArrayList<Priority>();
        try {
            Statement stmt = PostgreConnector.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(SELECT_TABLE_PRIORITY);

            while (resultSet.next()) {
                Priority tempProject = new Priority();

                String priorityId = resultSet.getString("priority_id");
                String name = resultSet.getString("name");

                if(priorityId == null && name == null)// bad way
                    continue;

                tempProject.setPriorityId(priorityId);
                tempProject.setPriorityName(name);

                priority.add(tempProject);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return priority;
    }
}
