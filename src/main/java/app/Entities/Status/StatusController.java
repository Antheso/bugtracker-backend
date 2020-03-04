package app.Entities.Status;

import io.javalin.http.Handler;


public class StatusController {

    public static Handler fetchAllStatus = ctx -> {
        ctx.json(StatusDao.getStatuses());
    };
}
