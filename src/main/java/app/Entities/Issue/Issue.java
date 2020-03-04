package app.Entities.Issue;

import app.Entities.Project.Project;
import app.Entities.User.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;


@JsonDeserialize(using = IssueDeserializer.class)
public class Issue {
    String id;
    String statusId;
    String priorityId;
    String summary;
    String description;
    String assigneId;
    String projectId;
    Project project;
    User assignee;

    public Issue(){}

    public Issue(String summary, String description, String priorityId, String statusId, Project project, User assignee) {
        this.summary = summary;
        this.description = description;
        this.priorityId = priorityId;
        this.statusId = statusId;
        this.project = project;
        this.assignee = assignee;
    }

    public Issue (
            String statusId,
            String priorityId,
            String issueId,
            String summary,
            String description,
            String assigneId,
            String projectId
    ) {
        this.statusId = statusId;
        this.priorityId = priorityId;
        this.summary = summary;
        this.description = description;
        this.assigneId = assigneId;
        this.projectId = projectId;
    }

    public Issue (ResultSet resultSet) {
        try {
            this.statusId = resultSet.getString("status_id");
            this.priorityId = resultSet.getString("priority_id");
            this.id = resultSet.getString("issue_id");
            this.summary = resultSet.getString("summary");
            this.description = resultSet.getString("description");
            this.assigneId = resultSet.getString("assigne_id");
            this.projectId = resultSet.getString("project_id");
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(String priorityId) {
        this.priorityId = priorityId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssigneId() {
        return assigneId;
    }

    public void setAssigneId(String assigneId) {
        this.assigneId = assigneId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
