package org.example.board.config.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtService {

    public static final String ISSUER = "moz1mozi.com";
    public static final int EXP = 900000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";


    private final SecretKey key;
    public JwtService(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public SecretKey getKey() {
        return key;
    }

    public String createAccessToken(Authentication authentication) {

        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .issuer(ISSUER)
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + EXP))
                .claim("username", username)
                .claim("roles", authorities)
                .signWith(key)
                .compact();


    }

    public String createRefreshToken(Authentication authentication) {
        String username = authentication.getName();
        long refreshExp = 7 * 24 * 60 * 60 * 1000; // 7일

        return Jwts.builder()
                .issuer(ISSUER)
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + refreshExp))
                .signWith(key)
                .compact();
    }

    public Claims verify(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token.replace(TOKEN_PREFIX, ""))
                .getPayload();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token.replace(TOKEN_PREFIX, ""))
                .getPayload();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(key).build().
                    parseSignedClaims(authToken.replace(TOKEN_PREFIX, ""))
                    .getPayload();
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

}