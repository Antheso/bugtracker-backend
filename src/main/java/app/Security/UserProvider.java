package app.Security;

//import app.Entities.User.User;
import app.Entities.User.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

public class UserProvider {
    public static JWTProvider createHMAC512() {
        JWTGenerator<User> generator = (user, alg) -> {
            JWTCreator.Builder token = JWT.create()
                    .withClaim("userId", user.getUserId())
                    .withClaim("name", user.getName())
                    .withClaim("roleId", "0"); //user.getRoleId()
            return token.sign(alg);
        };

        Algorithm algorithm = Algorithm.HMAC256("very_secret");
        JWTVerifier verifier = JWT.require(algorithm).build();

        return new JWTProvider(algorithm, generator, verifier);
    }
}
