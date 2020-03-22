package app.Javalin;

import io.javalin.core.security.Role;

public enum Roles implements Role {
    ADMIN,
    GUEST,
    USER
}
