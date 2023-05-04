package gratis.contoh.api.util.jwt;

import java.time.Duration;
import java.time.Instant;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class Jwt {
	private static final Duration JWT_TOKEN_VALIDITY_ONE_HOUR = Duration.ofHours(1);
	private static final Duration JWT_TOKEN_VALIDITY_ONE_YEAR = Duration.ofDays(365);
	
    private Algorithm hmac512;
    private JWTVerifier verifier;
    private String issuer;
    
    public Jwt(String issuer, String secret) {
        this.issuer = issuer;
        this.hmac512 = Algorithm.HMAC512(secret);
        this.verifier = JWT.require(this.hmac512).build();
    }

	public String generateToken(JwtDetail user, boolean rememberMe) {
		final Instant now = Instant.now();
		
		return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("role", user.getRole())
                .withIssuer(issuer)
                .withIssuedAt(now)
                .withExpiresAt(now.plusMillis(rememberMe ? 
                		JWT_TOKEN_VALIDITY_ONE_YEAR.toMillis() : JWT_TOKEN_VALIDITY_ONE_HOUR.toMillis()))
                .sign(this.hmac512);
	}
	
	public JwtDetail validateAndGetDetail(String token) {
		try {
			DecodedJWT decodedJwt = verifier.verify(token);
            
            return JwtDetail.builder()
            		.username(decodedJwt.getSubject())
            		.role(decodedJwt.getClaim("role").asString())
            		.build();
        } catch (final JWTVerificationException verificationEx) {
            return null;
        }
	}
}
