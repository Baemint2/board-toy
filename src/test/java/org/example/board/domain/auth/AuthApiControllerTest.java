//package org.example.board.domain.auth;
//
//import org.example.board.config.auth.JwtService;
//import org.example.board.domain.user.service.UserSecurityService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(AuthApiController.class)
//@MockBean(JpaMetamodelMappingContext.class)
//class AuthApiControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private JwtService jwtService;
//
//    @MockBean
//    private UserSecurityService userSecurityService;
//
//    @Test
//    @WithMockUser
//    public void 리프레쉬토큰_검증() throws Exception{
//        //Given
//        String refreshToken = "someRefreshToken";
//        String newAccessToken = "newAccessToken";
//        String username = "tester";
//
//        when(jwtService.validateToken(refreshToken)).thenReturn(true);
//        when(jwtService.getUsernameFromToken(refreshToken)).thenReturn(username);
//
//        UserDetails userDetails = new User(username, "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        when(jwtService.createAccessToken(any(Authentication.class))).thenReturn(newAccessToken);
//
//        // When & Then
//        mockMvc.perform(post("/api/v1/user/refreshToken")
//                        .header("Cookie", "refreshToken=" + refreshToken)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json("{\"accessToken\":\"" + newAccessToken + "\",\"tokenType\":\"Bearer\"}"));
//    }
//
//}