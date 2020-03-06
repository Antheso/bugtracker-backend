package app.Entities.Status;

import app.Util.Response;
import io.javalin.http.Handler;
import java.util.ArrayList;

public class StatusController {
    public static Handler fetchAllStatus = ctx -> {
        ArrayList<Status> statusData = StatusDao.getStatuses();
        if(statusData != null){
            ctx.json(new Response(true, statusData));
        }
        else throw new Exception("Status not found");
    };
}
