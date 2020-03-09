package app.Entities.Type;

import app.DB.PostgreConnector;
import app.Entities.Status.StatusDao;
import app.Util.MyLogger;
import org.eclipse.jetty.util.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static app.DB.Query.SELECT_TABLE_TYPES;

public class TypeDao {
    private static MyLogger logger = MyLogger.getLogger(TypeDao.class);

    public static ArrayList<Type> getTypes() throws SQLException {
        ArrayList<Type> types = new ArrayList<Type>();
        Connection connection = PostgreConnector.createConnection();
        try {
            ResultSet resultSet = PostgreConnector.executeSQL(connection, SELECT_TABLE_TYPES);

            while (resultSet.next()) {
                String typeId = resultSet.getString("type_id");
                String typeName = resultSet.getString("name");

                if (!StringUtil.isEmpty(typeId) && !StringUtil.isEmpty(typeName))
                    types.add(new Type(typeId, typeName));
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            connection.close();
        }
        return types;
    }
}
