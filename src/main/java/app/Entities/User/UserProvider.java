package app.Entities.User;

import app.Entities.Type.TypeDao;
import app.Security.JWTGenerator;
import app.Security.JWTProvider;
import app.Util.Configuration;
import app.Util.MyLogger;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

public class UserProvider {
    private static MyLogger logger = MyLogger.getLogger(UserProvider.class);

    public static JWTProvider createHMAC512() {
        JWTGenerator<User> generator = (user, alg) -> {
            JWTCreator.Builder token = JWT.create()
                    .withClaim("userId", user.getUserId())
                    .withClaim("name", user.getName())
                    .withClaim("roleId", user.getRoleId());
            logger.info("Token created for user " + user.getUserId());
            return token.sign(alg);
        };

        Algorithm algorithm = Algorithm.HMAC256(Configuration.properties.getProperty("secret"));
        JWTVerifier verifier = JWT.require(algorithm).build();
        return new JWTProvider(algorithm, generator, verifier);
    }
}
