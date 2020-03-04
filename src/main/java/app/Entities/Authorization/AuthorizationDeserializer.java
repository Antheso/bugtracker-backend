package app.Entities.Authorization;

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

public class AuthorizationDeserializer extends JsonDeserializer<Authorization> {
    @Override
    public Authorization deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        final String login = node.get("login").asText();
        final String password = node.get("password").asText();

        return  null;
    }
}
