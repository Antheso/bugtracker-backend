package app.Security;

import app.Entities.User.User;
import app.Entities.User.UserDao;
import app.Exception.AuthorizationException;
import app.Javalin.Roles;
import app.Util.MyLogger;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.Role;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class JWTAccessManager implements AccessManager {
    private String userRoleClaim;
    private Map<String, Role> rolesMapping;
    private Role defaultRole;
    MyLogger myLogger = MyLogger.getLogger(JWTAccessManager.class);

    public JWTAccessManager(String userRoleClaim, Map<String, Role> rolesMapping, Role defaultRole) {
        this.userRoleClaim = userRoleClaim;
        this.rolesMapping = rolesMapping;
        this.defaultRole = defaultRole;
    }

//    private boolean isAuthorized(Context context) {
//        if (!JavalinJWT.containsJWT(context)) {
//            return false;
//        }
//
//        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(context);
//        return tokenStorage.get(jwt.getToken()) == null;
//    }


    private Role extractRole(Context context) throws SQLException {
        if (!JavalinJWT.containsJWT(context)) {
            return defaultRole;
        }

        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(context);
        String userLevel = jwt.getClaim("roleId").asString();

        User currentUser = UserDao.getValidUser(jwt);
        if (currentUser != null) {
            myLogger.info("User:" + currentUser.getFirstName() + " is autorized");
        }

        Role currentRole = Roles.values()[Integer.parseInt(userLevel)];
        return Optional.ofNullable(currentRole).orElse(defaultRole);
    }

    @Override
    public void manage(Handler handler, Context context, Set<Role> permittedRoles) throws Exception {
        Role role = extractRole(context);

        if (permittedRoles.contains(role)) {
            handler.handle(context);
        } else {
            throw new AuthorizationException("Unauthorized");
        }
    }
}