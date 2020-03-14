package app.Javalin;

import app.Entities.Authorization.AuthorizationController;
import app.Entities.Comment.CommentController;
import app.Entities.Issue.IssueController;
import app.Entities.Priority.PriorityController;
import app.Entities.Project.ProjectController;
import app.Entities.Status.StatusController;
import app.Entities.Type.TypeController;
import app.Entities.User.User;
import app.Entities.User.UserController;
import app.Security.JWTAccessManager;
import app.Security.JWTProvider;
import app.Security.JavalinJWT;
import app.Entities.User.UserProvider;
import app.Util.Configuration;
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
    public static Map<String, User> tokenStorage;
    public static Javalin app;
    public static JWTAccessManager accessManager;

    private static MyLogger logger = MyLogger.getLogger(JavalinManager.class);

    public static void start(Javalin app) {
        JavalinManager.app = app;

        Map<String, Role> rolesMapping = new HashMap<String, Role>() {{
            put("admin", Roles.ADMIN);
            put("user", Roles.USER);
            put("guest", Roles.GUEST);
        }};

        accessManager = new JWTAccessManager("level", rolesMapping, Roles.ANYONE);
        provider = UserProvider.createHMAC512();
        decodeHandler = JavalinJWT.createCookieDecodeHandler(provider);
        tokenStorage = new HashMap<String, User>();

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

        app.exception(Exception.class, (e, ctx) -> {
            String exceptionDetails = logger.errorWithOutString(e);
            ctx.status(500);
            ctx.json(new Response(false, exceptionDetails));
        });

        get(Path.Web.ISSUE, IssueController.fetchAllIssue, new HashSet<>(Arrays.asList(Roles.ANYONE, Roles.ADMIN)));

        get(Path.Web.ONE_ISSUE, IssueController.fetchIssueByID, Collections.singleton(Roles.ANYONE));
        post(Path.Web.ISSUE, IssueController.insertIssue, Collections.singleton(Roles.ANYONE));
        patch(Path.Web.ONE_ISSUE, IssueController.updateIssue, Collections.singleton(Roles.ANYONE));
        delete(Path.Web.ONE_ISSUE, IssueController.deleteIssue, Collections.singleton(Roles.ANYONE));

        get(Path.Web.PROJECTS, ProjectController.fetchAllProject, Collections.singleton(Roles.ANYONE));
        get(Path.Web.PRIORITY, PriorityController.fetchAllPriority, Collections.singleton(Roles.ANYONE));
        get(Path.Web.STATUS, StatusController.fetchAllStatus, Collections.singleton(Roles.ANYONE));
        get(Path.Web.TYPES, TypeController.fetchAllType, Collections.singleton(Roles.ANYONE));

        get(Path.Web.USERS, UserController.fetchAllUser, Collections.singleton(Roles.ANYONE));

        post(Path.Web.LOGIN, UserController.login, Collections.singleton(Roles.ANYONE));
        post(Path.Web.LOGOUT, UserController.logout, Collections.singleton(Roles.ANYONE));
        post(Path.Web.REGISTRATION, UserController.registration, Collections.singleton(Roles.ANYONE));

        get(Path.Web.COMMENT_BY_ISSUE, CommentController.fetchAllComment, Collections.singleton(Roles.ANYONE));
        post(Path.Web.COMMENT, CommentController.addComment, Collections.singleton(Roles.ANYONE));
    }
}
