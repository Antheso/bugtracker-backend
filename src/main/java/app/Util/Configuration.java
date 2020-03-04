package app.Util;
import com.google.common.io.Files;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

public class Configuration {
    private String path;
    private Properties properties;

    public Configuration(String path) {
        this.path = path;
    }

    private void setProperty(String key, Object value) {
        if (value != null) {
            properties.setProperty(key, value.toString());
        }
    }

    public Properties getProperties() {
        properties = new Properties();
        Yaml yaml = new Yaml();

        try {
            FileInputStream inputStream = new FileInputStream(new File(path));
            Map<String, Object> obj = yaml.load(inputStream);
            setProperty("database_host", obj.get("database_host"));
            setProperty("database_port", obj.get("database_port"));
            setProperty("database_database", obj.get("database_database"));
            setProperty("database_user", obj.get("database_user"));
            setProperty("database_password", obj.get("database_password"));
            setProperty("port", obj.get("port"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}