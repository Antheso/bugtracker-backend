package app.Entities.Authorization;

import app.Entities.User.User;
import app.Entities.User.UserDao;
import app.Javalin.JavalinManager;
import app.Security.JavalinJWT;
import app.Util.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;
import java.util.ArrayList;

import static app.Javalin.JavalinManager.tokenStorage;

public class AuthorizationController {
    public static Handler login = ctx -> {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(ctx.body());

        final String login = node.get("login").asText();
        final String password = node.get("password").asText();

        ArrayList<User> users = UserDao.getUser(login);

        if(users == null) {
            throw new Exception("You entered incorrect login/password");
        }
        User tempUser = users.get(0);

        String token = JavalinManager.provider.generateToken(tempUser);

        JavalinJWT.addTokenToCookie(ctx, token);
        tokenStorage.put(token, tempUser);

        ctx.json(new Response(Response.Status.OK, tempUser));
    };
}
