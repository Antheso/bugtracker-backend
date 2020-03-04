package app.Entities.User;

import app.DB.PostgreConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static app.DB.Query.*;

public class UserDao {
    public static ArrayList<User> users;

    public static ArrayList<User> getUsers()
    {
        users = new ArrayList<User>();
        try {
            Statement stmt = PostgreConnector.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(SELECT_TABLE_USERS);

            while (resultSet.next()) {
                User tempUser = new User();

                String userId = resultSet.getString("user_id");
                String name = resultSet.getString("name");

                if(userId == null && name == null)// bad way
                    continue;

                tempUser.setUserId(userId);
                tempUser.setName(name);

                users.add(tempUser);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static ArrayList<User> getUser(String login)
    {
        users = new ArrayList<User>();
        try {
            ResultSet resultSet = PostgreConnector.connection
                    .createStatement()
                    .executeQuery(SELECT_USER_BY_LOGIN(login));

            while (resultSet.next()) {
                User tempUser = new User();

                String userId = resultSet.getString("user_id");
                String password = resultSet.getString("password");
                String roleId = resultSet.getString("role_id");

                if(password == null && userId == null && roleId == null)// bad way
                    continue;

                tempUser.setUserId(userId);
                tempUser.setPassword(password);
                tempUser.setRoleId(roleId);

                users.add(tempUser);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }


        return users;
    }
}
