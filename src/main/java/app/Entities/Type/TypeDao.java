package app.Entities.Type;

import app.DB.PostgreConnector;

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
            Statement stmt = PostgreConnector.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(SELECT_TABLE_TYPES);

            while (resultSet.next()) {
                Type tempType = new Type();

                String typeId = resultSet.getString("type_id");
                String typeName = resultSet.getString("name");

                if(typeId == null && typeName == null)// bad way
                    continue;

                tempType.setTypeId(typeId);
                tempType.setTypeName(typeName);

                types.add(tempType);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }
}
