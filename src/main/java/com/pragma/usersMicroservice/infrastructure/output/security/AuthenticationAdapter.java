package com.pragma.usersMicroservice.infrastructure.output.security;

import com.pragma.usersMicroservice.domain.model.User;
import com.pragma.usersMicroservice.domain.spi.IJwtProviderPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Adapter class that implements JWT token generation and validation.
 * <p>
 * This class serves as a bridge between the domain layer's JWT requirements
 * and the actual implementation using the JJWT library.
 * It allows the domain to remain agnostic of the specific libraries used for JWT handling.
 * </p>
 */
@Slf4j
@Component
public class AuthenticationAdapter implements IJwtProviderPort {


    @Value("${jwt.secret}")
    private String secret;

    private SecretKey key;

    /**
     * Initializes the secret key for JWT signing after the properties are set.
     * <p>
     * This method converts the secret string into a SecretKey object that can be used for signing JWT tokens.
     * It ensures that the key is properly initialized before any token generation occurs.
     * </p>
     */
    @PostConstruct
    public void init() {
        log.info("Initializing JWT secret key");
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        log.info("Generating token for user: {}", user.getEmail());
        claims.put("role", user.getRole().getName().toString());
        log.info("User role added to claims: {}", user.getRole().getName().toString());
        claims.put("userId", user.getId());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    @Override
    public String getEmailFromToken(String token) {
        log.info("Extracting email from token: {}", token);
        return getClaims(token).getSubject();
    }

    @Override
    public String getRoleFromToken(String token) {
        log.info("Extracting role from token: {}", token);
        return getClaims(token).get("role", String.class);
    }


    @Override
    public boolean validateToken(String token) {
        try {
            log.info("Validating token: {}", token);
            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    private SecretKey getKey() {
        log.info("Retrieving JWT signing key");
        return this.key;
    }

    private Claims getClaims(String token) {
        log.info("Parsing claims from token: {}", token);
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
