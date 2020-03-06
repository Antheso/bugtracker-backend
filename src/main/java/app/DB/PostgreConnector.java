package app.DB;

import app.Util.Configuration;
import java.sql.*;
import java.util.Properties;

public class PostgreConnector {
    static public Connection connection;
    static Properties properties = new Configuration("/config/configuration.yml").getProperties();

    //  Database credentials
    static String DB_URL = "jdbc:postgresql://" + properties.getProperty("database_host") + ":"
            + properties.getProperty("database_port") + "/" + properties.getProperty("database_database");
    static String USER = properties.getProperty("database_user");
    static String PASS = properties.getProperty("database_password");

    public static ResultSet executeSQL(String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }

    public static PreparedStatement createStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    public static void endConnection() throws SQLException {
        if(connection != null)
        {
            connection.close();
        }
    }

    public static void createConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}