package app.Entities.Project;

import io.javalin.http.Handler;

public class ProjectController {

    public static Handler fetchAllProject = ctx -> {
        ctx.json(ProjectDao.getProjects());
    };
}
