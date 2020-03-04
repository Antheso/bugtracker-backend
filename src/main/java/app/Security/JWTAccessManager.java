package app.Security;

import app.Entities.Authorization.Authorization;
import app.Javalin.Roles;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.Role;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static app.Javalin.JavalinManager.tokenStorage;

public class JWTAccessManager implements AccessManager {
    private String userRoleClaim;
    private Map<String, Role> rolesMapping;
    private Role defaultRole;

    public JWTAccessManager(String userRoleClaim, Map<String, Role> rolesMapping, Role defaultRole) {
        this.userRoleClaim = userRoleClaim;
        this.rolesMapping = rolesMapping;
        this.defaultRole = defaultRole;
    }

    private boolean isAuthorized(Context context) {

        if(!JavalinJWT.containsJWT(context)){
            return false;
        }

        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(context);
        if(tokenStorage.get(jwt.getToken()) != null){
            return true;
        }
        return false;
    }


    private Role extractRole(Context context)
    {
        if (!JavalinJWT.containsJWT(context)) {
            return defaultRole;
        }

        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(context);
        String userLevel = jwt.getClaim("roleId").asString();
        context.result(userLevel);

        String token = jwt.getToken();
        Authorization d = tokenStorage.get(token);
        if( d != null){
            System.out.println("IS autorization");
        }

        if(userLevel.contains("0")) {
            defaultRole = Roles.ADMIN;
        }

        return Optional.ofNullable(rolesMapping.get(userLevel)).orElse(defaultRole);
    }

    @Override
    public void manage(Handler handler, Context context, Set<Role> permittedRoles) throws Exception {
        Role role = extractRole(context);
        //permittedRoles.contains(role)
        if (isAuthorized(context)) {
            handler.handle(context);
        } else {
            context.status(401).result("Unauthorized");
            //context.redirect("/api/login");
        }

        handler.handle(context);
    }
}