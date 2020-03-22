package app.Javalin;

import app.Entities.Comment.CommentController;
import app.Entities.Issue.IssueController;
import app.Entities.Priority.PriorityController;
import app.Entities.Project.ProjectController;
import app.Entities.Status.StatusController;
import app.Entities.Type.TypeController;
import app.Entities.User.User;
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
    public static Map<String, User> tokenStorage;
    public static Javalin app;
    public static JWTAccessManager accessManager;

    private static MyLogger logger = MyLogger.getLogger(JavalinManager.class);

    public static void start(Javalin app) {
        JavalinManager.app = app;

        Map<String, Role> rolesMapping = new HashMap<String, Role>() {{
            put("admin", Roles.ADMIN);
            put("guest", Roles.GUEST);
            put("user", Roles.USER);
        }};

        accessManager = new JWTAccessManager("level", rolesMapping, Roles.GUEST);
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

        get(Path.Web.ISSUE, IssueController.fetchFilteredIssue, new HashSet<>(Arrays.asList(Roles.GUEST, Roles.ADMIN)));

        get(Path.Web.ONE_ISSUE, IssueController.fetchIssueByID, new HashSet<>(Arrays.asList(Roles.GUEST, Roles.ADMIN)));
        post(Path.Web.ISSUE, IssueController.insertIssue, Collections.singleton(Roles.ADMIN));
        patch(Path.Web.ONE_ISSUE, IssueController.updateIssue, Collections.singleton(Roles.ADMIN));
        delete(Path.Web.ONE_ISSUE, IssueController.deleteIssue, Collections.singleton(Roles.ADMIN));

        get(Path.Web.PROJECTS, ProjectController.fetchAllProject, new HashSet<>(Arrays.asList(Roles.GUEST, Roles.ADMIN)));
        get(Path.Web.PRIORITY, PriorityController.fetchAllPriority, new HashSet<>(Arrays.asList(Roles.GUEST, Roles.ADMIN)));
        get(Path.Web.STATUS, StatusController.fetchAllStatus, new HashSet<>(Arrays.asList(Roles.GUEST, Roles.ADMIN)));
        get(Path.Web.TYPES, TypeController.fetchAllType, new HashSet<>(Arrays.asList(Roles.GUEST, Roles.ADMIN)));

        get(Path.Web.USERS, UserController.fetchAllUser, new HashSet<>(Arrays.asList(Roles.GUEST, Roles.ADMIN)));

        post(Path.Web.LOGIN, UserController.login, new HashSet<>(Arrays.asList(Roles.GUEST, Roles.ADMIN)));
        post(Path.Web.LOGOUT, UserController.logout, new HashSet<>(Arrays.asList(Roles.GUEST, Roles.ADMIN)));
        post(Path.Web.REGISTRATION, UserController.registration, new HashSet<>(Arrays.asList(Roles.GUEST, Roles.ADMIN)));

        get(Path.Web.COMMENT_BY_ISSUE, CommentController.fetchAllComment, new HashSet<>(Arrays.asList(Roles.GUEST, Roles.ADMIN)));
        post(Path.Web.COMMENT, CommentController.addComment, Collections.singleton(Roles.ADMIN));

        app.exception(AuthorizationException.class, (e, ctx) -> {
            ctx.status(403);
            ctx.json(new Response(false, "e.getMessage()"));
        });

        app.exception(Exception.class, (e, ctx) -> {
            String exceptionDetails = logger.errorWithOutString(e);
            ctx.status(500);
            ctx.json(new Response(false, exceptionDetails));
        });
    }
}
