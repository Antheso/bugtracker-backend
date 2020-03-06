package app.DB;
import app.Util.Configuration;

import java.io.File;
import java.sql.*;

import java.util.Properties;

import static app.DB.Query.SELECT_TABLE_PRIORITY;

public class PostgreConnector {
    Properties properties = new Configuration(new File(PostgreConnector.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent() + "/config/configuration.yml").getProperties();

    //  Database credentials
    final String DB_URL = "jdbc:postgresql://" + properties.getProperty("database_host") + ":"
            + properties.getProperty("database_port") + "/" + properties.getProperty("database_database");
    final String USER = properties.getProperty("database_user");
    final String PASS = properties.getProperty("database_password");

    static public Connection connection;
    static public PreparedStatement preparedStatement;

    public PostgreConnector() {
        Init();
    }

    public void Init() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    public static ResultSet executeSQL(String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }

    public static PreparedStatement createStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

}