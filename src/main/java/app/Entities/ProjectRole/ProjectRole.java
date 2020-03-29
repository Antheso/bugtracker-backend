package app.Entities.ProjectRole;

public class ProjectRole {
    String projectRoleId;
    String projectRoleName;

    public ProjectRole(String projectRoleId, String projectRoleName) {
        this.projectRoleId = projectRoleId;
        this.projectRoleName = projectRoleName;
    }

    public String getProjectRoleId() {
        return projectRoleId;
    }

    public void setProjectRoleId(String projectRoleId) {
        this.projectRoleId = projectRoleId;
    }

    public String getProjectRoleName() {
        return projectRoleName;
    }

    public void setProjectRoleName(String projectRoleName) {
        this.projectRoleName = projectRoleName;
    }

    @Override
    public String toString() {
        return "Status{" +
                "statusId='" + projectRoleId + '\'' +
                ", statusName='" + projectRoleName + '\'' +
                '}';
    }
}
