package app.Entities.User;

import app.Util.Response;
import io.javalin.http.Handler;
import java.util.ArrayList;

public class UserController {
    public static Handler fetchAllUser = ctx -> {
        ArrayList<User> userData = UserDao.getUsers();
        if(userData != null)
        {
            ctx.json(new Response(true, userData));
        }
        else throw new Exception("User not found");
    };
}
