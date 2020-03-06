package app.Entities.Type;

import app.DB.PostgreConnector;
import org.eclipse.jetty.util.StringUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static app.DB.Query.SELECT_TABLE_TYPES;

public class TypeDao {

    public static ArrayList<Type> types;

    public static ArrayList<Type> getTypes()
    {
        types = new ArrayList<Type>();
        try {
            ResultSet resultSet = PostgreConnector.executeSQL(SELECT_TABLE_TYPES);

            while (resultSet.next()) {
                String typeId = resultSet.getString("type_id");
                String typeName = resultSet.getString("name");

                if(StringUtil.isEmpty(typeId) && StringUtil.isEmpty(typeName))
                    continue;

                types.add(new Type(typeId, typeName));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }
}
