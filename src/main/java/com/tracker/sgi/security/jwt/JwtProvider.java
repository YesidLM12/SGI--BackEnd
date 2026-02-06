package com.tracker.sgi.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.tracker.sgi.security.services.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
@Profile("!test")
public class JwtProvider {
    private JwtProperties jwtProperties;
    private Key secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }

    private Key getSecretKey() {
        return secretKey;
    }

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    public void validateConfig() {
        String secret = jwtProperties.getSecret();

        if(secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT_SECRET no está configurado o es demasiado corto");
        }
    }


    public String generateToken(UserDetails userDetails) {
        /*
         * Convierte el UserDetails a UserDetailsImpl
         */
        UserDetailsImpl user = (UserDetailsImpl) userDetails;

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

        /*
         * Genera el token JWT con
         * username
         * userID
         * fecha de creación
         * fecha de expiración
         * algoritmo de firma
         */
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userID", user.getId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extraer username (email)
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // validar token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // verificar si el token ha expirado
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
