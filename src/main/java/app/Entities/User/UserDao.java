package app.Entities.User;

import app.DB.PostgreConnector;
import app.Entities.Type.TypeDao;
import app.Util.MyLogger;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.uuid.impl.UUIDUtil;
import org.eclipse.jetty.util.StringUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import static app.DB.Query.*;

public class UserDao {
    private static MyLogger logger = MyLogger.getLogger(TypeDao.class);

    public static ArrayList<User> getUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        Connection connection = PostgreConnector.createConnection();
        try
        {
            PostgreConnector.createConnection();
            ResultSet resultSet = PostgreConnector.executeSQL(connection, SELECT_TABLE_USERS);

            while (resultSet.next())
            {
                String userId = resultSet.getString("user_id");
                String name = resultSet.getString("first_name");

                if(StringUtil.isEmpty(userId) && StringUtil.isEmpty(name))
                    continue;

                users.add(new User(userId, name));
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
        }
        finally
        {
            connection.close();
        }
        return users;
    }

    public static ArrayList<User> getUser(String login) throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        Connection connection = PostgreConnector.createConnection();
        try
        {
            PostgreConnector.createConnection();
            PreparedStatement preparedStatement = PostgreConnector.createStatement(connection, SELECT_USER_BY_LOGIN);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String userId = resultSet.getString("user_id");
                String password = resultSet.getString("password");
                String roleId = resultSet.getString("role_id");
                String name = resultSet.getString("first_name");

                if(!StringUtil.isEmpty(password) && !StringUtil.isEmpty( userId ) && !StringUtil.isEmpty(roleId) && !StringUtil.isEmpty(name))
                    users.add(new User(userId, password, roleId, name));
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
        }
        finally
        {
            connection.close();
        }
        return users;
    }

    public static User getValidUser(DecodedJWT jwt) throws SQLException {
        User user = null;
        Connection connection = PostgreConnector.createConnection();
        try
        {
            PostgreConnector.createConnection();
            PreparedStatement preparedStatement = PostgreConnector.createStatement(connection, SELECT_VALID_USER);
            preparedStatement.setInt(1, Integer.parseInt(jwt.getClaim("roleId").asString()));
            preparedStatement.setObject(2, UUIDUtil.uuid(jwt.getClaim("userId").asString()));
            preparedStatement.setString(3, jwt.getClaim("userName").asString());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String userId = resultSet.getString("user_id");
                String roleId = resultSet.getString("role_id");
                String name = resultSet.getString("first_name");

                if(!StringUtil.isEmpty( userId ) && !StringUtil.isEmpty(roleId) && !StringUtil.isEmpty(name)) {
                    user = new User(userId, null, roleId, name);
                } else {
                    throw new Exception("Not found user");
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e);
        }
        finally
        {
            connection.close();
        }
        return user;
    }

    public static ArrayList<User> getUserById(String id) throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        Connection connection = PostgreConnector.createConnection();
        try
        {
            PostgreConnector.createConnection();
            PreparedStatement preparedStatement = PostgreConnector.createStatement(connection, SELECT_USER_BY_ID);
            preparedStatement.setObject(1, UUID.fromString(id));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String userId = resultSet.getString("user_id");
                String name = resultSet.getString("name");

                if(StringUtil.isEmpty(userId) && StringUtil.isEmpty(name))
                    continue;

                users.add(new User(userId, name));
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
        }
        finally
        {
            connection.close();
        }
        return users;
    }

    public static int addUser(String firstName, String lastName, String password, String email, String roleId) throws SQLException {
        Connection connection = PostgreConnector.createConnection();
        try {
            PostgreConnector.createConnection();
            PreparedStatement statement = PostgreConnector.createStatement(connection, INSERT_USER_PARAMS);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, password);
            statement.setString(4, email);
            statement.setInt(5, Integer.parseInt(roleId));

            return statement.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
        } finally {
            connection.close();
        }

        return 0;
    }
}
