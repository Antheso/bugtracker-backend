package app.Entities.User;


import io.javalin.http.Handler;

public class UserController {
    public static Handler fetchAllUser = ctx -> {
        try {
            ctx.json(UserDao.getUsers());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    };
}
