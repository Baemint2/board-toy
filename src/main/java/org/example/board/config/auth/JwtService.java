package org.example.board.config.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

    public static final String SUBJECT = "moz1mozi";
    public static final int EXP = 900000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
    public SecretKey key = Jwts.SIG.HS512.key().build();

    public String create(Authentication authentication) {

        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(SUBJECT)
                .expiration(new Date(System.currentTimeMillis() + EXP))
                .claim("username", username)
                .claim("roles", authorities)
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

}