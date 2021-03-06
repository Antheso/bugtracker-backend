package app.Javalin;

import app.Entities.Comment.CommentController;
import app.Entities.Issue.IssueController;
import app.Entities.Priority.PriorityController;
import app.Entities.Project.ProjectController;
import app.Entities.ProjectRole.ProjectRoleController;
import app.Entities.Status.StatusController;
import app.Entities.Type.TypeController;
import app.Entities.User.UserController;
import app.Exception.AuthorizationException;
import app.Security.JWTAccessManager;
import app.Security.JWTProvider;
import app.Security.JavalinJWT;
import app.Entities.User.UserProvider;
import app.Util.MyLogger;
import app.Util.Path;
import app.Util.Response;
import io.javalin.Javalin;
import io.javalin.core.security.Role;
import io.javalin.http.Handler;
import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class JavalinManager {
    private static JavalinManager instance;
    private static Handler decodeHandler;
    public static JWTProvider provider;
    public static Javalin app;
    public static JWTAccessManager accessManager;

    private static MyLogger logger = MyLogger.getLogger(JavalinManager.class);

    public static void start(Javalin app) {
        JavalinManager.app = app;

        Map<String, Role> rolesMapping = new HashMap<String, Role>() {{
            put("anyone", Roles.ANYONE);
            put("user", Roles.USER);
            put("admin", Roles.ADMIN);
        }};

        accessManager = new JWTAccessManager("level", rolesMapping, Roles.ANYONE);
        provider = UserProvider.createHMAC512();
        decodeHandler = JavalinJWT.createCookieDecodeHandler(provider);

        app.config.accessManager(accessManager);
        app.routes(JavalinManager::addEndpoints);

        logger.info("Start Javalin Manager");
    }

    public static JavalinManager getInstance() {
        if (instance == null) {
            instance = new JavalinManager();
        }
        return instance;
    }

    private static void addEndpoints() {
        app.before(decodeHandler);

        get(Path.Web.ISSUE, IssueController.fetchFilteredIssue, new HashSet<>(Arrays.asList(Roles.ANYONE , Roles.USER)));

        get(Path.Web.ONE_ISSUE, IssueController.fetchIssueByID, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));
        post(Path.Web.ISSUE, IssueController.insertIssue, Collections.singleton(Roles.USER));
        patch(Path.Web.ONE_ISSUE, IssueController.updateIssue, Collections.singleton(Roles.USER));
        delete(Path.Web.ONE_ISSUE, IssueController.deleteIssue, Collections.singleton(Roles.USER));

        get(Path.Web.PRIORITY, PriorityController.fetchAllPriority, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));
        get(Path.Web.STATUS, StatusController.fetchAllStatus, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));
        get(Path.Web.TYPES, TypeController.fetchAllType, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));

        get(Path.Web.USERS, UserController.fetchAllUser, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));

        post(Path.Web.LOGIN, UserController.login, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));
        post(Path.Web.LOGOUT, UserController.logout, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));
        post(Path.Web.REGISTRATION, UserController.registration, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));

        get(Path.Web.COMMENT_BY_ISSUE, CommentController.fetchAllComment, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));
        post(Path.Web.COMMENT, CommentController.addComment, Collections.singleton(Roles.USER));

        get(Path.Web.CUSER, UserController.fetchCurrentUser, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));

        get(Path.Web.PROJECTROLES, ProjectRoleController.fetchAllProjectRole, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));

        post(Path.Web.PROJECTS, ProjectController.insertProject, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));
        patch(Path.Web.PROJECTS, ProjectController.updateProject, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));
        get(Path.Web.PROJECTS, ProjectController.selectProjectsByCurrentUser, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));
        get(Path.Web.PROJECTSID, ProjectController.selectProjectAndUserByProjectId, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));

        get(Path.Web.REGISTRATIONTOKEN, UserController.registrationVerify, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));

        get(Path.Web.USERRESEND, UserController.resendVerified, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.USER)));
        app.exception(AuthorizationException.class, (e, ctx) -> {
            ctx.status(403);
            ctx.json(new Response(Response.Status.ERROR, e.getMessage()));
        });

        app.exception(IllegalStateException.class, (e, ctx) -> {
            String exceptionDetails = logger.errorWithOutString(e);
            ctx.status(401);
            ctx.json(new Response(Response.Status.ERROR, "invalid password/password"));
        });

        app.exception(Exception.class, (e, ctx) -> {
            String exceptionDetails = logger.errorWithOutString(e);
            ctx.status(500);
            ctx.json(new Response(Response.Status.ERROR, exceptionDetails));
        });
    }
}
