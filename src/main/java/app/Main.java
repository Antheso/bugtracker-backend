package app;

import app.Javalin.JavalinManager;
import app.Util.Configuration;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        final Configuration configuration = new Configuration();

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
        }).start(configuration.getIntProperty("javalin_port", "7071"));

        JavalinManager.start(app);
    }
}
