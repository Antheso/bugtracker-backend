package app.Entities.Status;

import app.DB.PostgreConnector;
import org.eclipse.jetty.util.StringUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static app.DB.Query.SELECT_TABLE_PRIORITY;

public class StatusDao {
    public static ArrayList<Status> getStatuses()
    {
        ArrayList<Status> statuses = new ArrayList<Status>();
        try
        {
            PostgreConnector.createConnection();
            ResultSet resultSet = PostgreConnector.executeSQL(SELECT_TABLE_PRIORITY);

            while (resultSet.next())
            {
                String statusId = resultSet.getString("status_id");
                String name = resultSet.getString("name");

                if(StringUtil.isEmpty(statusId) && StringUtil.isEmpty(name))
                    continue;

                statuses.add(new Status(statusId, name));
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
        return statuses;
    }
}
