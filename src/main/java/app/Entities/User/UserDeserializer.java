package app.Entities.User;

import app.Util.Configuration;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class UserDeserializer extends JsonDeserializer<User> {
    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);

        final String password = node.get("password").asText();
//        int salt = Integer.parseInt(Configuration.properties.getProperty("salt"));
//        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(salt));
        final String firstName = node.get("firstName").asText();
        final String lastName = node.get("lastName").asText();
        final String email = node.get("email").asText();

        return new User(firstName, lastName, password, email, "1");
    }
}
