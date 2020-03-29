package app.Entities.Project;

import app.Entities.Issue.Issue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.jetty.util.StringUtil;

import java.io.IOException;

public class ProjectDeserializer extends JsonDeserializer<Project> {
    @Override
    public Project deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        final String projectId = node.get("projectId").asText();
        final String projectName = node.get("projectName").asText();
        final String description = node.get("description").asText();
        final boolean isPrivate = node.get("isPrivate").asBoolean();

        return new Project(projectId, projectName, null, description, isPrivate);
    }
}
