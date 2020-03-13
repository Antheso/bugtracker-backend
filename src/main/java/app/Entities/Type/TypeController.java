package app.Entities.Type;

import app.Util.Response;
import io.javalin.http.Handler;

import java.util.ArrayList;

public class TypeController {
    public static Handler fetchAllType = ctx -> {
        ArrayList<Type> typeData = TypeDao.getTypes();
        if (typeData != null) {
            ctx.json(new Response(Response.Status.OK, typeData));
        } else {
            throw new Exception("Type not found");
        }
    };
}
