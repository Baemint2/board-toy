    package org.example.board.domain.email.controller;

    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.example.board.domain.email.service.AuthenticationService;
    import org.example.board.domain.email.service.VerificationService;
    import org.example.board.domain.user.repository.TemporaryRepository;
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
    import org.springframework.security.test.context.support.WithMockUser;
    import org.springframework.test.context.junit.jupiter.SpringExtension;
    import org.springframework.test.web.servlet.MockMvc;
    import org.springframework.test.web.servlet.setup.MockMvcBuilders;
    import org.springframework.web.context.WebApplicationContext;

    import java.util.Map;

    import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
    import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


    @ExtendWith(SpringExtension.class)
    @WebMvcTest(controllers = EmailApiController.class)
    @MockBean(JpaMetamodelMappingContext.class)
    class EmailApiControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AuthenticationService authenticationService;
        @MockBean
        private TemporaryRepository temporaryRepository;
        @MockBean
        private VerificationService verificationService;

        @MockBean
        private UserService userService;


        @BeforeEach
        public void setUp(WebApplicationContext webApplicationContext){
            this.mockMvc = MockMvcBuilders
                    .webAppContextSetup(webApplicationContext)
                    .apply(springSecurity())
                    .build();
        }


        @Test
        @WithMockUser
        public void 이메일_인증번호_인증성공() throws Exception {
            // Given
            String email = "test@example.com";
            String code = "123456";
            Mockito.when(verificationService.verifyCode(email, code)).thenReturn(true);

            // When & Then
            mockMvc.perform(post("/api/email/verifyCode")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(Map.of("email", email, "code", code)))
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("인증 성공"));
        }


        @Test
        @WithMockUser
        public void 유효하지_않은_인증번호() throws Exception {
            //Given
            String email = "test@example.com";
            String code = "invalid_code";
            Mockito.when(verificationService.verifyCode(email, code)).thenReturn(false);
            // When & Then
            mockMvc.perform(post("/api/email/verifyCode")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(Map.of("email", email, "code", code)))
                            .with(csrf()))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("인증 실패"));
        }

    }