package app.Javalin;

import app.Entities.Authorization.Authorization;
import app.Entities.Issue.Issue;
import app.Entities.User.User;
import app.Entities.User.UserController;
import app.Entities.User.UserDao;
import app.Security.JWTProvider;
import app.Security.JavalinJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;
import javax.servlet.http.Cookie;

import java.util.ArrayList;
import java.util.Map;

import static app.Javalin.JavalinManager.tokenStorage;

public class AuthorizationController {
    public AuthorizationController() {
    }

    public static Handler login = ctx -> {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(ctx.body());

        final String login = node.get("login").asText();
//        final String password = new Hash(node.get("password").asText()).getHash();
        final String password = node.get("password").asText();

        if (!login.matches("[\\w-_]{" + login.length() + "}")) {
            ctx.result("Login must contain only [a-z0-9_-].");
            return;
        }

        if (login.matches("[а-яА-ЯЄєІіїЇҐґ]+")) {
            ctx.result("The login should contain only English words.");
            return;
        }

        ArrayList<User> users = UserDao.getUser(login);

        if(users == null) {
            ctx.result("You entered incorrect login/password");
            return;
        }
        User tempUser = users.get(0);

        String token = JavalinManager.provider.generateToken(tempUser);

        JavalinJWT.addTokenToCookie(ctx, token);
        tokenStorage.put(token, new Authorization(login, tempUser.roleGet()));
        System.out.println("USER LOGIN AND MUST HAVE COOKIE");

        ctx.json("{ \"ok\": \"true\" }");
    };
}
