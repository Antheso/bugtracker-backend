package app.Entities.User;

import app.Javalin.JavalinManager;
import app.Security.JavalinJWT;
import app.Util.Configuration;
import app.Util.Response;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

public class UserController {
    public static Handler fetchAllUser = ctx -> {
        ArrayList<User> userData = UserDao.getUsers();
        if (!userData.isEmpty()) {
            ctx.json(new Response(true, userData));
        } else {
            throw new Exception("User not found");
        }
    };

    public static Handler fetchCurrentUser = ctx -> {
        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(ctx);
        User tempUser = UserDao.getValidUser(jwt);
        if (tempUser != null) {
            ctx.json(new Response(true, tempUser));
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
        if (!BCrypt.checkpw(password, pas)) {
            ctx.status(401);
            ctx.result("User not found");
            return;
        }

        String token = JavalinManager.provider.generateToken(tempUser);
        JavalinJWT.addTokenToCookie(ctx, token);

        ctx.json(new Response(true, "success login"));
    };

    public static Handler logout = ctx -> {
        JavalinJWT.removeTokenToCookie(ctx);
        ctx.json(new Response(true, "logout"));
    };

    public static Handler registration = ctx -> {
        ObjectMapper om = new ObjectMapper();
        User user = om.readValue(ctx.body(), User.class);

        if (user != null) {
            int insertRow = UserDao.addUser(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getRoleId()
            );

            if (insertRow > 0) {
                ctx.json(new Response(true, "success registration"));
            } else {
                throw new Exception("Insert user failed");
            }
        } else {
            throw new Exception("Insert user failed");
        }
    };


}
