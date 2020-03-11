package app.Entities.User;

import app.Entities.Issue.IssueDeserializer;
import app.Javalin.Roles;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.javalin.core.security.Role;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonDeserialize(using = UserDeserializer.class)
public class User {
    @JsonInclude(NON_NULL)
    private String name;
    @JsonInclude(NON_NULL)
    private String userId;
    @JsonInclude(NON_NULL)
    private String lastName;
    @JsonInclude(NON_NULL)
    private String loginName;
    @JsonInclude(NON_NULL)
    private String roleId;
    @JsonInclude(NON_NULL)
    private String password;
    @JsonInclude(NON_NULL)
    private String email;

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

    public User(String login, String password, String firstName, String lastName, String email) {
        this.loginName = login;
        this.password = password;
        this.name = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public Role roleGet() {
        return Roles.values()[Integer.parseInt(getRoleId())];
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", lastName='" + lastName + '\'' +
                ", loginName='" + loginName + '\'' +
                ", roleId='" + roleId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
