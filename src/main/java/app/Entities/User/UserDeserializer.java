package app.Entities.User;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class UserDeserializer extends JsonDeserializer<User> {
    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);

        final String login = node.get("login").asText();
        final String password = node.get("password").asText();
        final String firstName = node.get("firstName").asText();
        final String lastName = node.get("lastName").asText();
        final String email = node.get("email").asText();


        return new User(login, password, firstName, lastName, email);
    }
}
