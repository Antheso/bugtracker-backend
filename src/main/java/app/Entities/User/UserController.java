package app.Entities.User;

import app.Javalin.JavalinManager;
import app.Security.JavalinJWT;
import app.Util.Response;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;

import java.util.ArrayList;

import static app.Javalin.JavalinManager.tokenStorage;

public class UserController {
    public static Handler fetchAllUser = ctx -> {
        ArrayList<User> userData = UserDao.getUsers();
        if (userData != null) {
            ctx.json(new Response(true, userData));
        } else {
            throw new Exception("User not found");
        }
    };

    public static Handler login = ctx -> {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(ctx.body());

        final String login = node.get("login").asText();
        final String password = node.get("password").asText();

        ArrayList<User> users = UserDao.getUser(login);

        if (users.isEmpty()) {
            ctx.status(401);
            ctx.result("You entered incorrect login/password");
            return;
        }
        User tempUser = users.get(0);
        String pas = tempUser.getPassword();
        if (!pas.equals(password)) {
            ctx.status(401);
            ctx.result("User not found");
            return;
        }

        String token = JavalinManager.provider.generateToken(tempUser);
        JavalinJWT.addTokenToCookie(ctx, token);
        tokenStorage.put(token, tempUser);

        ctx.json(new Response(true, "login"));
    };

    public static Handler logout = ctx -> {
        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(ctx);
        String token = jwt.getToken();

        if (tokenStorage.get(token) != null) {
            tokenStorage.remove(token);
            JavalinJWT.removeTokenToCookie(ctx);
            ctx.json(new Response(true, "logout"));
        } else {
            ctx.json(new Response(false, "not found"));
        }
    };

    public static Handler registration = ctx -> {
        int inserRow = 0;
        ObjectMapper om = new ObjectMapper();
        User user = om.readValue(ctx.body(), User.class);

        if (user != null) {
            inserRow = UserDao.addUser(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getRoleId()
            );
        } else {
            throw new Exception("Insert user failed");
        }

        if (inserRow > 0) {
            ctx.json(new Response(true, "success"));
        } else {
            throw new Exception("Insert user failed");
        }
    };
}
