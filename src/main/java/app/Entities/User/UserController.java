package app.Entities.User;

import app.Exception.AuthorizationException;
import app.Javalin.JavalinManager;
import app.Notification.Email.EmailNotificator;
import app.Notification.NotificationType;
import app.Security.JWTProvider;
import app.Security.JavalinJWT;
import app.Security.Password;
import app.Util.Configuration;
import app.Util.Response;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.impl.UUIDUtil;
import io.javalin.http.Handler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.Date;

import com.auth0.jwt.JWT;

public class UserController {
    private static EmailNotificator emailNotificator = new EmailNotificator();

    public static Handler fetchAllUser = ctx -> {
        ArrayList<User> userData = UserDao.getUsers();

        if (userData.isEmpty()) {
            throw new Exception("Got empty user list!");
        }
        ctx.json(new Response(Response.Status.OK, userData));
    };

    public static Handler fetchCurrentUser = ctx -> {
        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(ctx);

        if(jwt == null) {
            throw new AuthorizationException("Not found user");
        }

        User tempUser = UserDao.getValidUser(jwt);
        if (tempUser == null) {
            throw new AuthorizationException("Not found user");
        }

        ctx.json(new Response(Response.Status.OK, tempUser));
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
        if (!Password.check(password, pas)) {
            ctx.status(401);
            ctx.result("User not found");
            return;
        }

        String token = JavalinManager.provider.generateToken(tempUser);
        Date expdate = new Date ();
        expdate.setTime (expdate.getTime() + 36000*1000);
        ctx.header("set-cookie", "jwt="+ token + " ; SameSite=Lax; Path=/; Expires=" + expdate);
        ctx.json(new Response(Response.Status.OK, "success login"));
    };

    public static Handler logout = ctx -> {
        ctx.header("set-cookie", "jwt=; SameSite=Lax; Path=/; Expires= Thu, 01 Jan 1970 00:00:00 GMT");
        ctx.json(new Response(Response.Status.OK, "logout"));
    };

    public static Handler registration = ctx -> {
        ObjectMapper om = new ObjectMapper();
        User user = om.readValue(ctx.body(), User.class);
        if (user == null) {
            throw new Exception("Insert user failed");
        }

        int insertRow = UserDao.addUser(
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getEmail(),
                user.getRoleId()
        );

        if (insertRow <= 0) {
            throw new Exception("Insert user failed");
        }

        user = UserDao.getUser(user.getEmail()).get(0);

        emailNotificator.sendUserNotification(user,
                NotificationType.UserNotification.COMPLETE_REGISTRATION,
                user.getEmail());

        ctx.json(new Response(Response.Status.OK, "success registration"));
    };

    public static Handler registrationVerify = ctx -> {

        String token = ctx.pathParam("token");
        DecodedJWT jwt = JWT.decode(token);

        User user = UserDao.getValidUser(jwt);

        if(user==null){
            ctx.status(400);
            ctx.result("Error");
        }

        UserDao.setVerified(jwt);
        ctx.redirect("https://t1.sumdu-tss.site/login");
    };

    public static Handler resendVerified = ctx -> {
        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(ctx);

        User user = UserDao.getValidUser(jwt);

        if(user==null){
            ctx.status(400);
            ctx.result("Error");
        }

        emailNotificator.sendUserNotification(user,
                NotificationType.UserNotification.COMPLETE_REGISTRATION,
                user.getEmail());

        ctx.json(new Response(Response.Status.OK, "success resend"));
    };
}
