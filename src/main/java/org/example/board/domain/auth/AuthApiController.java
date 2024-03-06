//package org.example.board.domain.auth;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.board.common.utils.CookieUtils;
//import org.example.board.config.auth.JwtService;
//import org.example.board.domain.user.service.UserSecurityService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Collections;
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//public class AuthApiController {
//
//    private final JwtService jwtService;
//    private final UserSecurityService userSecurityService;
//
//    @PostMapping("/api/v1/user/refreshToken")
//    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
//        String refreshToken = CookieUtils.getCookieValue(request, "refreshToken");
//        log.info("리프레쉬 토큰 가져오기 = {}", refreshToken);
//        // 리프레쉬 토큰 값 가져와서 리프레쉬 토큰 값이 널이 아니거나, 유효한 토큰이면
//        if(refreshToken != null && jwtService.validateToken(refreshToken)) {
//
//            //유저 네임에 리프레쉬 토큰을 넣는다.
//            String username = jwtService.getUsernameFromToken(refreshToken);
//            log.info("username = {}", username);
//            UserDetails userDetails = userSecurityService.loadUserByUsername(username);
//            log.info("userDetails = {}", userDetails);
//            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            log.info("authentication = {}", authentication);
//            // 액세스 토큰 생성
//            String newJwt = jwtService.createAccessToken(authentication);
//            log.info("새로운 액세스 토큰 생성 = {}", newJwt);
//            return ResponseEntity.ok(Map.of("accessToken", newJwt, "tokenType", "Bearer"));
//        } else {
//            return ResponseEntity.badRequest().body(Map.of("error", "리프레쉬 토큰이 검증되지 않았습니다."));
//        }
//    }
//
//    @GetMapping("/api/v1/user/check-auth")
//    public ResponseEntity<?> checkUserAuth(HttpServletRequest request) {
//        String refreshToken = CookieUtils.getCookieValue(request, "refreshToken");
//
//        if(refreshToken != null && jwtService.validateToken(refreshToken)) {
//           String username = jwtService.getUsernameFromToken(refreshToken);
//
//            return ResponseEntity.ok(Map.of("isLoggedIn", true, "username", username));
//        } else {
//            return ResponseEntity.ok(Map.of("isLoggedIn", false));
//        }
//    }
//}
