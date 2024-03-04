package org.example.board.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.board.config.auth.JwtService;
import org.example.board.domain.user.dto.UserCreateDto;
import org.example.board.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserApiController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        when(userService.checkIfValidOldPassword(anyString())).thenReturn(true);
        when(jwtService.create(any(Authentication.class))).thenReturn("mockedJwtToken");
    }

    @Test
    @WithMockUser
    void join_ShouldReturnUserId_WhenSignupIsSuccessful() throws Exception {
        // Given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .username("tester")
                .nickname("히모지")
                .email("test@example.com")
                .password1("qwer1234")
                .password2("qwer1234")
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/user/sign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void 비밀번호_변경_확인() throws Exception {
        //Given
        String requestBody = """
                {
                    "currentPassword": "oldPassword",
                    "newPassword": "newPassword123",
                    "confirmPassword": "newPassword123"
                }
                """;
        //When && Then
        mockMvc.perform(post("/api/v1/user/password/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("비밀번호가 성공적으로 변경되었습니다."));

    }

    @Test
    @WithMockUser
    public void 비밀번호_변경_실패() throws Exception {
        //Given
        String requestBody = """
                {
                    "currentPassword": "oldPassword",
                    "newPassword": "newPassword123",
                    "confirmPassword": "newPassword124"
                }
                """;
        //When && Then
        mockMvc.perform(post("/api/v1/user/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("비밀번호가 성공적으로 변경되었습니다."));

    }

    @Test
    @WithMockUser
    public void 아이디찾기_성공() throws Exception {
        //Given
        String email = "test@example.com";
        String nickname = "히모지";
        String username = "tester";

        when(userService.findUsernameByEmail(email, nickname)).thenReturn(username);

        //When & Then
        mockMvc.perform(post("/api/v1/user/findUsername")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(Map.of("email", email, "nickname", nickname)))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"username\":\"" + username + "\"}"));

    }

    @Test
    @WithMockUser
    public void 아이디찾기_실패() throws Exception {
        //Given
        String nickname = "히모지";
        String email = "test@example.com";

        when(userService.findUsernameByEmail(email, nickname)).thenReturn(null);

        //When & Then
        mockMvc.perform(post("/api/v1/user/findUsername")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(Map.of("email", email, "nickname", nickname)))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"해당 이메일로 등록된 사용자가 없습니다.\"}"));
    }

    @Test
    @WithMockUser
    public void JWT_로그인_성공() throws Exception {
        //Given
        String username = "tester";
        String password = "qwer1234";
        String token = "mockedJwtToken";


        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(jwtService.create(any(Authentication.class))).thenReturn(token);

        // When & Then
        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(Map.of("username", username, "password", password)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(token))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }


}