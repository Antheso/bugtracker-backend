package app.Entities.Issue;

import app.Entities.Project.Project;
import app.Entities.User.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@JsonDeserialize(using = IssueDeserializer.class)
public class Issue {
    //id - issue_number //FAQ
    @JsonInclude(NON_NULL)
    String id;
    @JsonInclude(NON_NULL)
    String statusId;
    @JsonInclude(NON_NULL)
    String priorityId;
    @JsonInclude(NON_NULL)
    String typeId;
    @JsonInclude(NON_NULL)
    String summary;
    @JsonInclude(NON_NULL)
    String description;
    @JsonInclude(NON_NULL)
    String number;
    @JsonInclude(NON_NULL)
    Project project;
    @JsonInclude(NON_NULL)
    User assignee;
    @JsonInclude(NON_NULL)
    User author;
    @JsonInclude(NON_NULL)
    String deprecated;

    public Issue(String issueId, String summary, String number) {
        this.id = issueId;
        this.summary = summary;
        this.number = number;
    }

    public Issue(String issueId,
                 String summary,
                 String description,
                 String priorityId,
                 String typeId,
                 String statusId,
                 String number,
                 Project project,
                 User assignee,
                 User author,
                 String deprecated
    ) {
        this.id = issueId;
        this.summary = summary;
        this.description = description;
        this.priorityId = priorityId;
        this.typeId = typeId;
        this.statusId = statusId;
        this.number = number;
        this.project = project;
        this.assignee = assignee;
        this.author = author;
        this.deprecated = deprecated;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(String deprecated) {
        this.deprecated = deprecated;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id='" + id + '\'' +
                ", statusId='" + statusId + '\'' +
                ", priorityId='" + priorityId + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", project=" + project +
                ", assignee=" + assignee +
                ", author=" + author +
                '}';
    }
}
