package app;

import app.Javalin.JavalinManager;
import app.Util.Configuration;
import io.javalin.Javalin;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        final Properties properties = new Configuration("/config/configuration.yml").getProperties();
        final int javalinPort = Integer.parseInt(properties.getProperty("javalin_port", "7071"));

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
        }).start(javalinPort);

        JavalinManager.start(app);
    }
}
