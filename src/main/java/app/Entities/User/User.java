package app.Entities.User;

import app.Javalin.Roles;
import io.javalin.core.security.Role;

public class User {
    private String name;
    private String userId;
    private String lastName;
    private String loginName;
    private String roleId;
    private String password;

    public User(String userId, String name) {
        this.name = name;
        this.userId = userId;
    }

    public User(String userId, String password, String roleId, String name) {
        this.userId = userId;
        this.password = password;
        this.roleId = roleId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role roleGet(){
        return Roles.values()[Integer.parseInt(getRoleId())];
    }
}
