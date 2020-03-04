package app.Entities.Comment;

import app.Entities.Issue.Issue;
import app.Entities.Project.Project;
import app.Entities.User.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class CommentDeserializer extends JsonDeserializer<Comment> {
    @Override
    public Comment deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        final String issueId = node.get("issueId").asText();
        final String userId = node.get("user").get("id").asText();
        final String text = node.get("text").asText();
        final String timestamp = node.get("timestamp").asText();

        return new Comment(issueId, text, userId, timestamp);
    }
}
