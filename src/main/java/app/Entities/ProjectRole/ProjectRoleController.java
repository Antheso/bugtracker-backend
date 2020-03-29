package app.Entities.ProjectRole;

import app.Util.Response;
import io.javalin.http.Handler;

import java.util.ArrayList;

public class ProjectRoleController {
    public static Handler fetchAllProjectRole = ctx -> {
        ArrayList<ProjectRole> projectRoleData = ProjectRoleDao.getProjectRole();
        if (projectRoleData != null) {
            ctx.json(new Response(Response.Status.OK, projectRoleData));
        } else {
            throw new Exception("Status not found");
        }
    };
}
