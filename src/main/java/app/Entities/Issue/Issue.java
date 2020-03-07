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
    @JsonInclude(NON_NULL)
    String id;
    @JsonInclude(NON_NULL)
    String statusId;
    @JsonInclude(NON_NULL)
    String priorityId;
    @JsonInclude(NON_NULL)
    String summary;
    @JsonInclude(NON_NULL)
    String description;
    @JsonInclude(NON_NULL)
    String assigneId;
    @JsonInclude(NON_NULL)
    String projectId;
    @JsonInclude(NON_NULL)
    Project project;
    @JsonInclude(NON_NULL)
    User assignee;
    @JsonInclude(NON_NULL)
    User author;

    public Issue(String issueId, String summary)
    {
        this.id = issueId;
        this.summary = summary;
    }

//    public Issue(
//        String issueId,
//        String summary,
//        String description,
//        String priorityId,
//        String statusId,
//        String projectId,
//        String assigneeId,
//        String authorId
//    )
//    {
//        this.id = issueId;
//        this.summary = summary;
//        this.description = description;
//        this.priorityId = priorityId;
//        this.statusId = statusId;
//        this.project = new Project(projectId, null);
//        this.assignee = new User(assigneeId, null);
//        this.author = new User(authorId, null);
//    }

    public Issue(
        String issueId,
        String summary,
        String description,
        String priorityId,
        String statusId,
        Project project,
        User assignee,
        User author
    )
    {
        this.id = issueId;
        this.summary = summary;
        this.description = description;
        this.priorityId = priorityId;
        this.statusId = statusId;
        this.project = project;
        this.assignee = assignee;
        this.author = author;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id='" + id + '\'' +
                ", statusId='" + statusId + '\'' +
                ", priorityId='" + priorityId + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", assigneId='" + assigneId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", project=" + project +
                ", assignee=" + assignee +
                ", author=" + author +
                '}';
    }
}
