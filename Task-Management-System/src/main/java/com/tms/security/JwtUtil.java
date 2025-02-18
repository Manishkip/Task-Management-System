package com.tms.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String base64Secret;

    private String secret = String.valueOf(generateSecretKey());
    // Replace with your secret key
    //private long expiration = 86400000L; // 24 hours expiration
    private long expiration = 3600000L; // 1 hour expiration (60 * 60 * 1000 ms)

    // Method to generate the JWT token with roles
    public String generateToken(String username, Set<String> roles) {
        SecretKey key = getSecretKey(); // Get the secret key
        return Jwts.builder()
                .setSubject(username) // Set the username as subject
                .claim("roles", roles) // Include roles as a custom claim
                .setIssuedAt(new Date()) // Set issue time
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Set expiration time
                .signWith(key) // Sign the token with the secret key
                .compact();
    }

    // Extract claims from the token
    private Claims extractClaims(String token) {
        try {
            JwtParser jwtParser = Jwts.parser()  // Using parser() for older versions
                    .setSigningKey(secret)  // Secret key for validation
                    .build();

            // Parse and get claims from JWT
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (JwtException | IllegalArgumentException e) {
            // Log and handle invalid or malformed tokens
            throw new RuntimeException("Invalid token or signature", e);
        }
    }


    // Extract username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a specific claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate the token
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    // Method to generate a secure secret key for HS256
    // Generate a secure secret key for HS256 (used only during initial setup)
    public SecretKey generateSecretKey() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Secure 256-bit key
        System.out.println("Generated secret key (Base64): " + Base64.getEncoder().encodeToString(key.getEncoded()));
        return key;
    }

    // Decode base64 secret key from application.properties
    private SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(base64Secret);
        return new javax.crypto.spec.SecretKeySpec(decodedKey, "HmacSHA256");
    }
}
