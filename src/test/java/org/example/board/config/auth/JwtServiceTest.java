package org.example.board.config.auth;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {


    private SecretKey key = Jwts.SIG.HS512.key().build();
    
    @Test
    public void JWT토큰_생성_검증() {
        String subject = "test123";
        Date now = new Date();
        long validityInMillis = 900000;

        //토큰 생성
        String token = Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + validityInMillis))
                .signWith(key)
                .compact();
        
        // 토큰 검증
        String parsedSubject = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();


        assertThat(parsedSubject).isEqualTo(subject);
    }
}