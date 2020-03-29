package app.Entities.User;

import app.Javalin.Roles;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.javalin.core.security.Role;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonDeserialize(using = UserDeserializer.class)
public class User {
    @JsonInclude(NON_NULL)
    private String firstName;
    @JsonInclude(NON_NULL)
    private String userId;
    @JsonInclude(NON_NULL)
    private String lastName;
    @JsonInclude(NON_NULL)
    private String roleId;
    @JsonInclude(NON_NULL)
    private String password;
    @JsonInclude(NON_NULL)
    private String email;
    @JsonInclude(NON_NULL)
    private String projectRoleId;
    @JsonInclude(NON_NULL)
    private boolean isVerified;

    public User(String userId, String firstName) {
        this.firstName = firstName;
        this.userId = userId;
    }

    public User(String userId, String password, String roleId, String firstName) {
        this.userId = userId;
        this.password = password;
        this.roleId = roleId;
        this.firstName = firstName;
    }

    public User(String firstName, String lastName, String password, String email, String roleId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.roleId = roleId;
    }

    public User(String firstName, String lastName, String userId, String email, String projectRoleId, String roleId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.email = email;
        this.projectRoleId = projectRoleId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getProjectRoleId() {
        return projectRoleId;
    }

    public void setProjectRoleId(String projectRoleId) {
        this.projectRoleId = projectRoleId;
    }

    public boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + firstName + '\'' +
                ", userId='" + userId + '\'' +
                ", lastName='" + lastName + '\'' +
                ", roleId='" + roleId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
