package org.example.board.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.board.domain.user.dto.UserCreateDto;
import org.example.board.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Mockito.when(userService.checkIfValidOldPassword(anyString())).thenReturn(true);
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
                .andExpect(MockMvcResultMatchers.content().string("비밀번호가 성공적으로 변경되었습니다."));

    }
}