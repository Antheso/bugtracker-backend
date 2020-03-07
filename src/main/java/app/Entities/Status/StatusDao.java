package app.Entities.Status;

import app.DB.PostgreConnector;
import org.eclipse.jetty.util.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static app.DB.Query.SELECT_TABLE_PRIORITY;
import static app.DB.Query.SELECT_TABLE_STATUSES;

public class StatusDao {
    public static ArrayList<Status> getStatuses() throws SQLException {
        ArrayList<Status> statuses = new ArrayList<Status>();
        Connection connection = PostgreConnector.createConnection();
        try
        {
            ResultSet resultSet = PostgreConnector.executeSQL(connection, SELECT_TABLE_STATUSES);

            while (resultSet.next())
            {
                String statusId = resultSet.getString("status_id");
                String name = resultSet.getString("name");

                if(!StringUtil.isEmpty(statusId) && !StringUtil.isEmpty(name))
                    statuses.add(new Status(statusId, name));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            connection.close();
        }
        return statuses;
    }
}
