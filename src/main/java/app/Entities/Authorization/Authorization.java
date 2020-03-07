package app.Entities.Authorization;

import io.javalin.core.security.Role;
import java.util.Objects;

public class Authorization {
    private String login;
    private String name;
    private Role role;

    public Authorization(String login, Role role, String name) {
        this.login = login;
        this.role = role;
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "Authorization{"
                + "login='" + login + '\''
                + ", role=" + role
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Authorization that = (Authorization) o;
        return Objects.equals(login, that.login) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, role);
    }
}