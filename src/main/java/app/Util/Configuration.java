package app.Util;
import app.DB.PostgreConnector;
import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class Configuration {
    private String path;
    public static Properties properties;

    public Configuration()
    {
        final String filePath = "/config/configuration.yml";
        String relatePath = PostgreConnector.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        relatePath = new File(relatePath).getParent();
        this.path = relatePath + filePath;

        readProperties();
    }

    private Properties readProperties()
    {
        properties = new Properties();
        Yaml yaml = new Yaml();

        try
        {
            FileInputStream inputStream = new FileInputStream(new File(path));
            Map<String, Object> configurationMap = yaml.load(inputStream);
            setProperty("database_host", configurationMap.get("database_host"));
            setProperty("database_port", configurationMap.get("database_port"));
            setProperty("database_database", configurationMap.get("database_database"));
            setProperty("database_user", configurationMap.get("database_user"));
            setProperty("database_password", configurationMap.get("database_password"));
            setProperty("port", configurationMap.get("port"));
            setProperty("secret", configurationMap.get("secret"));
            setProperty("javalin_port", configurationMap.get("javalin_port"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return properties;
    }

    private void setProperty(String key, Object value)
    {
        if (value != null)
        {
            properties.setProperty(key, value.toString());
        }
    }

    public int getIntProperty(String key, String defaultName)
    {
        return Integer.parseInt(properties.getProperty(key, defaultName));
    }
}