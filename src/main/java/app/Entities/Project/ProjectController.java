package app.Entities.Project;

import app.Entities.Issue.IssueDao;
import app.Entities.ProjectUserRole.ProjectUserRole;
import app.Entities.ProjectUserRole.ProjectUserRoleList;
import app.Entities.User.User;
import app.Entities.User.UserDao;
import app.Exception.AuthorizationException;
import app.Security.JavalinJWT;
import app.Util.Response;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;

import java.util.ArrayList;

public class ProjectController {

    public static Handler fetchAllProject = ctx -> {

        ArrayList<Project> projectData = ProjectDao.getProjects();
        if (projectData != null) {
            ctx.json(new Response(Response.Status.OK, projectData));
        } else {
            throw new Exception("Project not found");
        }
    };

    public static Handler insertProject = ctx -> {

        ObjectMapper om = new ObjectMapper();
        Project project = om.readValue(ctx.body(), Project.class);

        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(ctx);
        Project oldProject = ProjectDao.getProjectByProjectId(project.getProjectId());

        if(oldProject != null) {
            ctx.status(400);
            ctx.result("This project id already exist, please select another id");
            return;
        }

        User author = UserDao.getValidUser(jwt);
        if (author == null) {
            throw new AuthorizationException("Insert bad author failed");
        }

        int insertRow = ProjectDao.addProject(
                project.getProjectId(),
                project.getProjectName(),
                author.getUserId(),
                project.getDescription(),
                project.isPrivate()
        );

        if (insertRow <= 0) {
            throw new Exception("Insert project failed");
        }

        ProjectUserRoleList projectUserRoleList = om.readValue(ctx.body(), ProjectUserRoleList.class);
        projectUserRoleList.add(new ProjectUserRole("2", author.getUserId()));
        int insertRowUser = ProjectDao.addProjectUsers(projectUserRoleList, project.getProjectId());

        ctx.json(new Response(Response.Status.OK, project));
    };

    public static Handler updateProject = ctx -> {

        ObjectMapper om = new ObjectMapper();
        Project project = om.readValue(ctx.body(), Project.class);
        Project oldProject = ProjectDao.getProjectByProjectId(project.getProjectId());

        if(oldProject == null) {
            ctx.status(400);
            ctx.json(new Response(Response.Status.ERROR, "This project id not existed, pleas select another id"));
            return;
        }

        int updateRow = ProjectDao.updateProject(
                project.getProjectId(),
                project.getProjectName(),
                project.getCreateBy(),
                project.getDescription(),
                project.isPrivate()
        );

        if (updateRow <= 0) {
            throw new Exception("Update project failed");
        }

        ProjectUserRoleList newProjectUserRoleList = om.readValue(ctx.body(), ProjectUserRoleList.class);
        ProjectUserRoleList oldProjectUserRoleList = ProjectDao.getProjectUserRoleByProjectId(project.getProjectId());
        ProjectUserRoleList updateProjectUserRoleList =  ProjectUserRoleList.intersection(newProjectUserRoleList, oldProjectUserRoleList);

        if(updateProjectUserRoleList.isEmpty()){
            ProjectDao.addProjectUsers(newProjectUserRoleList, project.getProjectId());
            ProjectDao.deleteProjectUsers(oldProjectUserRoleList, project.getProjectId());
        } else {
            ProjectUserRoleList newInsertList = ProjectUserRoleList.minus(updateProjectUserRoleList, newProjectUserRoleList);
            ProjectUserRoleList newDeleteList = ProjectUserRoleList.minus(updateProjectUserRoleList, oldProjectUserRoleList);

            if(!newInsertList.isEmpty())
                ProjectDao.addProjectUsers(newInsertList, project.getProjectId());
            if(!newDeleteList.isEmpty())
                ProjectDao.deleteProjectUsers(newDeleteList, project.getProjectId());

            ProjectDao.updateProjectUsers(updateProjectUserRoleList, project.getProjectId());
        }

        ctx.json(new Response(Response.Status.OK, "success"));
    };

    public static Handler deleteProject = ctx -> {

        String projectId = ctx.pathParam("id");
        IssueDao.deleteAllIssueByProject(projectId);

        int deleteRow = ProjectDao.deleteProject(projectId);
        if (deleteRow <= 0) {
            throw new Exception("Delete project failed");
        }

        ctx.json(new Response(Response.Status.OK, "delete"));
    };

    public static Handler selectProjectsByCurrentUser = ctx -> {

        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(ctx);

        if (jwt == null) {
            throw new AuthorizationException("Not found user");
        }

        User tempUser = UserDao.getValidUser(jwt);
        if (tempUser == null) {
            throw new AuthorizationException("Not found user");
        }

        ArrayList<Project> projects = ProjectDao.getProjectsByUser(tempUser.getUserId());

        ctx.json(new Response(Response.Status.OK, projects));
    };

    public static Handler selectProjectAndUserByProjectId = ctx -> {

        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(ctx);

        if (jwt == null) {
            throw new AuthorizationException("Not found user");
        }

        User tempUser = UserDao.getValidUser(jwt);
        if (tempUser == null) {
            throw new AuthorizationException("Not found user");
        }
        String projectId = ctx.pathParam("id");
        Project project = ProjectDao.getProjectByProjectId(projectId);

        ArrayList<User> users = ProjectDao.getProjectUsersByProjectId(projectId, tempUser.getUserId());

        for (int i = 0; i < users.size(); i++){
            if(users.get(i).getUserId().equals(tempUser.getUserId())){
                project.setCurrUserRole(users.get(i).getProjectRoleId());
                users.remove(i);
            }
        }

        project.setUsers(users);

        ctx.json(new Response(Response.Status.OK, project));
    };
}
