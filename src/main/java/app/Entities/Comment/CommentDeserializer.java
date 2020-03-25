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

        final String issueNumber = node.get("issueId").asText();
        final String text = node.get("text").asText();
        final long timestamp = node.get("timestamp").asLong();

        return new Comment(issueNumber, null, text, null, timestamp);
    }
}
