package app.Javalin;

import app.DB.PostgreConnector;
import app.Entities.Authorization.Authorization;
import app.Entities.Comment.CommentController;
import app.Entities.Issue.IssueController;
import app.Entities.Priority.PriorityController;
import app.Entities.Project.ProjectController;
import app.Entities.Status.StatusController;
import app.Entities.Type.TypeController;
import app.Entities.User.UserController;
import app.Security.JWTAccessManager;

import app.Security.JWTProvider;
import app.Security.JavalinJWT;
import app.Security.UserProvider;
import app.Util.Path;
import io.javalin.Javalin;
import io.javalin.core.security.Role;
import io.javalin.http.Handler;

import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class JavalinManager {
    private static JavalinManager instance;

    public static Map<String, Authorization> tokenStorage;
    public static Javalin app;
    public static PostgreConnector db;

    public static JWTProvider provider;
    public static Handler decodeHandler;

    private JavalinManager(){}

    public static void start(Javalin app) {
        JavalinManager.app = app;
        db = new PostgreConnector();

        Map<String, Role> rolesMapping = new HashMap<String, Role>() {{
            put("admin", Roles.ADMIN);
            put("user", Roles.USER);
            put("guest", Roles.GUEST);
        }};

        provider = UserProvider.createHMAC512();
        decodeHandler = JavalinJWT.createCookieDecodeHandler(provider);

        tokenStorage = new HashMap<String, Authorization>();
        JWTAccessManager accessManager = new JWTAccessManager("level", rolesMapping, Roles.ANYONE);

        app.config.accessManager(accessManager);
        app.routes(JavalinManager::addEndpoints);
    }

    public static JavalinManager getInstance(){
        if(instance == null){
            instance = new JavalinManager();
        }
        return instance;
    }

    private static void addEndpoints() {
        app.before(decodeHandler);

        get(Path.Web.ISSUE, IssueController.fetchAllIssue, new HashSet<>(Arrays.asList(Roles.ANYONE,Roles.ADMIN)));

        get(Path.Web.ONE_ISSUE, IssueController.fetchIssueByID, Collections.singleton(Roles.ANYONE));
        post(Path.Web.ISSUE, IssueController.insertIssue, Collections.singleton(Roles.ANYONE));
        patch(Path.Web.ONE_ISSUE, IssueController.updateIssue, Collections.singleton(Roles.ANYONE));
        delete(Path.Web.ONE_ISSUE, IssueController.deleteIssue, Collections.singleton(Roles.ANYONE));

        get(Path.Web.PROJECTS, ProjectController.fetchAllProject, Collections.singleton(Roles.ANYONE));
        get(Path.Web.PRIORITY, PriorityController.fetchAllPriority, Collections.singleton(Roles.ANYONE));
        get(Path.Web.STATUS, StatusController.fetchAllStatus, Collections.singleton(Roles.ANYONE));
        get(Path.Web.TYPES, TypeController.fetchAllType, Collections.singleton(Roles.ANYONE));

        get(Path.Web.USERS, UserController.fetchAllUser, Collections.singleton(Roles.ANYONE));

        post(Path.Web.LOGIN, AuthorizationController.login, Collections.singleton(Roles.ANYONE));
        get(Path.Web.VALIDATION, AuthorizationController.login, Collections.singleton(Roles.ANYONE));

        get(Path.Web.COMMENT_BY_ISSUE, CommentController.fetchAllComment, Collections.singleton(Roles.ANYONE));
        post(Path.Web.COMMENT, CommentController.addComment, Collections.singleton(Roles.ANYONE));
    }
}
