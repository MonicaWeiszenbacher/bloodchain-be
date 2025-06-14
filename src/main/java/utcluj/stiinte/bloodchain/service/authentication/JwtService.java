package utcluj.stiinte.bloodchain.service.authentication;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    
    private final SecretKey secretKey;
    private final long jwtExpirationInMs;

    public JwtService(@Value("${app.jwtSecret}") String secret,
                            @Value("${app.jwtExpirationMs}") long jwtExpirationInMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    /**
     * Generates a JWT token for the given username.
     *
     * @param authentication subject for JWT
     * @return The generated JWT token string.
     */
    public String generateToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .claims() // Start building claims
                .subject(authentication.getName())
                .issuedAt(now)
                .expiration(expiryDate)
                .and() // Return to the main JwtBuilder
                .signWith(secretKey, Jwts.SIG.HS512) // Use HS512 for signing
                .compact();
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token The JWT token string.
     * @return The username (subject) extracted from the token.
     */
    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from a JWT token.
     *
     * @param token          The JWT token string.
     * @param claimsResolver A function to resolve the desired claim from the Claims.
     * @param <T>            The type of the claim.
     * @return The resolved claim.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Validates a JWT token.
     *
     * @param token The JWT token string.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException ex) {
            // Invalid JWT token
            System.err.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            // Expired JWT token
            System.err.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            // Unsupported JWT token
            System.err.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            // JWT claims string is empty
            System.err.println("JWT claims string is empty");
        }
        return false;
    }
}