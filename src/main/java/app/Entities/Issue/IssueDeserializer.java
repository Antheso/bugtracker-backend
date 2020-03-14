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

        final String projectId = node.get("project").get("projectId").asText();
        final String projectName = node.get("project").get("projectName").asText();

        final String assigneeId = node.get("assignee").get("userId").asText();
        final String assigneeName = node.get("assignee").get("name").asText();
        User assignee = new User(assigneeId, assigneeName);
        //Experemental
        //final String authorId = node.get("author").get("authorId").asText();
        //final String authorName = node.get("author").get("name").asText();
        //final String number = node.get("number").asText();
        // User author = new User(authorId, authorName);

        final String description = node.get("description").asText();
        final String summary = node.get("summary").asText();
        final String priorityId = node.get("priorityId").asText();

        final String typeId = node.get("typeId").asText();
        final String statusId = node.get("statusId").asText();

        Project project = new Project(projectId, projectName);
        project.setProjectId(projectId);

        return new Issue(null, summary, description, priorityId, typeId, statusId, null, project, assignee, null);
    }
}