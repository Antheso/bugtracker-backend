package app.Entities.Priority;

import app.DB.PostgreConnector;
import org.eclipse.jetty.util.StringUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static app.DB.Query.SELECT_TABLE_PRIORITY;

public class PriorityDao {
    public static ArrayList<Priority> getPriority()
    {
        ArrayList<Priority> priority = new ArrayList<Priority>();
        try
        {
            PostgreConnector.createConnection();
            ResultSet resultSet = PostgreConnector.executeSQL(SELECT_TABLE_PRIORITY);

            while (resultSet.next())
            {
                String priorityId = resultSet.getString("priority_id");
                String name = resultSet.getString("name");

                if(StringUtil.isEmpty(priorityId) && StringUtil.isEmpty(name))
                    continue;

                priority.add(new Priority(priorityId, name));
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
        return priority;
    }
}
