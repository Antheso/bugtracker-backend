package app.Entities.Issue;

import app.Entities.Project.Project;
import app.Entities.User.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class IssueDeserializer extends JsonDeserializer<Issue> {
    @Override
    public Issue deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        String projectId = node.get("project").get("projectId").asText();
        String projectName = node.get("project").get("projectName").asText();

        String assigneeId = node.get("assignee").get("userId").asText();
        String assigneeName = node.get("assignee").get("firstName").asText();
        User assignee = new User(assigneeId, assigneeName);
        //Experemental
        String authorId;
        String authorName;
        String id ="";
        User author = new User(null, null);
        try {
            id = node.get("number").asText();
            authorId = node.get("author").get("userId").asText();
            authorName = node.get("author").get("firstName").asText();
            author = new User(authorId, authorName);
        } catch (Exception ex) {

        }

        String description = node.get("description").asText();
        String summary = node.get("summary").asText();
        String priorityId = node.get("priorityId").asText();

        String typeId = node.get("typeId").asText();
        String statusId = node.get("statusId").asText();

        Project project = new Project(projectId, projectName);
        project.setProjectId(projectId);

        String number = "";
        if (!id.isEmpty()) {
           number = id.split("-")[1];
        }

        return new Issue(id, summary, description, priorityId, typeId, statusId, number, project, assignee, author, null);
    }
}
