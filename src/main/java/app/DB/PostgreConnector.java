package app.DB;

import app.Util.Configuration;
import java.sql.*;
import java.util.Properties;

public class PostgreConnector {
    static Properties properties = Configuration.properties;

    //  Database credentials
    static String DB_URL = "jdbc:postgresql://" + properties.getProperty("database_host") + ":"
            + properties.getProperty("database_port") + "/" + properties.getProperty("database_database");
    static String USER = properties.getProperty("database_user");
    static String PASS = properties.getProperty("database_password");

    public static ResultSet executeSQL(Connection connection, String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }

    public static PreparedStatement createStatement(Connection connection, String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    public static Connection createConnection()
    {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
//            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}