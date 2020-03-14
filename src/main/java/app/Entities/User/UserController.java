package app.Entities.User;

import app.Javalin.JavalinManager;
import app.Notification.Email.EmailNotificator;
import app.Notification.NotificationType;
import app.Security.JavalinJWT;
import app.Util.Response;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;

import java.util.ArrayList;

import static app.Javalin.JavalinManager.tokenStorage;

public class UserController {
    private static EmailNotificator emailNotificator = new EmailNotificator();

    public static Handler fetchAllUser = ctx -> {
        ArrayList<User> userData = UserDao.getUsers();
        ctx.json(new Response(Response.Status.OK, userData));
    };

    public static Handler login = ctx -> {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(ctx.body());

        final String login = node.get("login").asText();
        final String password = node.get("password").asText();

        ArrayList<User> users = UserDao.getUser(login);
        if (users.isEmpty()) {
            throw new Exception("You entered incorrect login/password");
        }

        User tempUser = users.get(0);

        String token = JavalinManager.provider.generateToken(tempUser);

        JavalinJWT.addTokenToCookie(ctx, token);
        tokenStorage.put(token, tempUser);

        ctx.json(new Response(Response.Status.OK, tempUser));
    };

    public static Handler logout = ctx -> {
        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(ctx);
        String token = jwt.getToken();

        if (tokenStorage.get(token) == null) {
            ctx.json(new Response(Response.Status.ERROR, "not found"));
        }

        tokenStorage.remove(token);
        ctx.json(new Response(Response.Status.OK, "logout"));
    };

    public static Handler registration = ctx -> {
        ObjectMapper om = new ObjectMapper();
        User user = om.readValue(ctx.body(), User.class);
        if (user == null) {
            throw new Exception("Insert user failed");
        }

        int insertRow = UserDao.addUser(
                user.getName(),
                user.getLastName(),
                user.getLoginName(),
                user.getPassword(),
                user.getEmail()
        );
        if (insertRow <= 0) {
            throw new Exception("Insert user failed");
        }

        ctx.json(new Response(Response.Status.OK, user));

        emailNotificator.sendUserNotification(user,
                NotificationType.UserNotification.COMPLETE_REGISTRATION,
                user.getEmail());
    };
}
