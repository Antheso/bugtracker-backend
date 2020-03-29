package app.Entities.ProjectUserRole;

public class ProjectUserRole {

    String projectId;
    String projectRoleId;
    String userId;

    public ProjectUserRole(String projectRoleId, String userId) {
        this.projectRoleId = projectRoleId;
        this.userId = userId;
    }

    public String getProjectRoleId() {
        return projectRoleId;
    }

    public void setProjectRoleId(String projectRoleId) {
        this.projectRoleId = projectRoleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    @Override
    public String toString() {
        return "Status{" +
                "statusId='" + projectRoleId + '\'' +
                ", statusName='" + userId + '\'' +
                '}';
    }
}
