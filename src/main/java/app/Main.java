package app;

import app.Javalin.JavalinManager;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;


public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
        }).start(getHerokuAssignedPort());

        JavalinManager.start(app);
    }

    private static int getHerokuAssignedPort() {
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            return Integer.parseInt(herokuPort);
        }
        return 7071;
    }

}
