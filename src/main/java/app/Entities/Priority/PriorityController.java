package app.Entities.Priority;

import app.Util.Response;
import io.javalin.http.Handler;

import java.util.ArrayList;

public class PriorityController {
    public static Handler fetchAllPriority = ctx -> {
        ArrayList<Priority> priorityData = PriorityDao.getPriority();
        if (priorityData != null) {
            ctx.json(new Response(true, priorityData));
        } else {
            throw new Exception("Priority not found");
        }
    };
}
