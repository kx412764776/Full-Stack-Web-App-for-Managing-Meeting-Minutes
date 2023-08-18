package com.apprenticeship.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * This class is used to generate a JWT token.
 */
@Service
public class JWTUtil {

    private static final String SECRET_KEY =
            "manage_your_apprenticeship_meetings_secret_key";

    public String issueToken(String subject) {
        return issueToken(subject, Map.of());
    }

    public String issueToken(String subject, String ...scopes) {
        return issueToken(subject, Map.of("scopes", scopes));
    }

    public String issueToken(String subject, List<String> scopes) {
        return issueToken(subject, Map.of("scopes", scopes));
    }

    // This method is used to generate a JWT token and set expiration time.
    public String issueToken(
            String subject,
            Map<String, Object> claims) {

        String token = Jwts.builder()
                .setClaims(claims) // set jwt claims in the payload
                .setSubject(subject) // set subject in the payload
                .setIssuer("http://apprenticeship.com") // set issuer in the payload
                .setIssuedAt(Date.from(Instant.now()))
                // set expiration time in the payload (15 days)
                .setExpiration(
                        Date.from(
                                Instant.now().plus(15, DAYS)
                        )
                )
                // set signature algorithm and secret key
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    // Decode the JWT token and return the claims.
    private Claims getClaims(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Set the key to verify the signature.
                .build()
                .parseClaimsJws(token) // Parse the token.
                .getBody(); // Get the claims.
        return claims;
    }

    // Return a Key object that is used to sign the JWT token.
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // check if the token is expired and the subject is the same as the username.
    public boolean isTokenValid(String jwt, String email) {
        String subject = getSubject(jwt);
        return subject.equals(email) && !isTokenExpired(jwt);
    }

    // check if the token is expired.
    private boolean isTokenExpired(String jwt) {
        return getClaims(jwt).getExpiration().before(Date.from(Instant.now()));
    }
}
