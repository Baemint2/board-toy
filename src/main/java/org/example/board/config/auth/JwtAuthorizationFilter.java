package org.example.board.config.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.user.entity.SiteUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String prefixJwt = request.getHeader(JwtService.HEADER);


        if (prefixJwt == null || !prefixJwt.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = prefixJwt.replace(JwtService.TOKEN_PREFIX, "");
        Jws<Claims> jws;
        try{
            jws = Jwts.parser()
                    .verifyWith(jwtService.key)
                    .build()
                    .parseSignedClaims(jwt);

            String username = jws.getPayload().getSubject();
            SiteUser siteUser = SiteUser.builder()
                    .username(username)
                    .build();

            Authentication authentication = new UsernamePasswordAuthenticationToken(siteUser.getUsername(), null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException ex) {
            log.info("토큰이 이상합니다.", ex);
        } finally {
            filterChain.doFilter(request, response);
        }

    }
}
