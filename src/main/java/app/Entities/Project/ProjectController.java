package app.Entities.Project;

import app.Util.Response;
import io.javalin.http.Handler;

import java.util.ArrayList;

public class ProjectController {
    public static Handler fetchAllProject = ctx -> {
        ArrayList<Project> projectData = ProjectDao.getProjects();
        if (projectData != null) {
            ctx.json(new Response(true, projectData));
        } else {
            throw new Exception("Project not found");
        }
    };
}
