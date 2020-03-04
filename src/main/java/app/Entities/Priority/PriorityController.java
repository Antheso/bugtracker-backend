package app.Entities.Priority;

import io.javalin.http.Handler;

public class PriorityController {
    public static Handler fetchAllPriority = ctx -> {
        ctx.json(PriorityDao.getPriority());
    };
}
