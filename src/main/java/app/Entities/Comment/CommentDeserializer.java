package app.Entities.Comment;

import app.Entities.User.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class CommentDeserializer extends JsonDeserializer<Comment> {
    @Override
    public Comment deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        final String issueId = node.get("issueId").asText();
        final String userId = node.get("user").get("id").asText();
        final String text = node.get("text").asText();
        final long timestamp = node.get("timestamp").asLong();

        final User user = new User(userId, null);

        return new Comment(issueId, null, text, user, timestamp);
    }
}
