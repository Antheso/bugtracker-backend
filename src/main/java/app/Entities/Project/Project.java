package app.Entities.Project;

import app.Entities.User.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

@JsonDeserialize(using = ProjectDeserializer.class)
public class Project {
    String projectId;
    String projectName;
    String createBy;
    String description;
    boolean isPrivate;

    String projectRoleId;
    ArrayList<User> users;
    String currUserRole;

    public Project(String projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }

    public Project(String projectId, String projectName, String createdBy, String description, boolean isPrivate) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.createBy = createdBy;
        this.description = description;
        this.isPrivate = isPrivate;
    }

    public Project(String projectId, String projectName, String description, boolean isPrivate, String projectRoleId) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.description = description;
        this.isPrivate = isPrivate;
        this.projectRoleId = projectRoleId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getProjectRoleId() {
        return projectRoleId;
    }

    public void setProjectRoleId(String projectRoleId) {
        this.projectRoleId = projectRoleId;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public String getCurrUserRole() {
        return currUserRole;
    }

    public void setCurrUserRole(String currUserRole) {
        this.currUserRole = currUserRole;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}
