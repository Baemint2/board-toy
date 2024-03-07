package org.example.board.domain.user.service;

import org.example.board.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @BeforeEach
//    void setup() {
//        SiteUser siteUser = SiteUser.builder()
//                .email("tester234@gmail.com")
//                .password(passwordEncoder.encode("qqqq1111"))
//                .username("tester234")
//                .nickname("테스터234")
//                .build();
//        userRepository.save(siteUser);
//    }
    @Test
    @WithMockUser(username = "tester234", password = "qqqq1111")
    public void 비밀번호_확인() {
        boolean result = userService.checkIfValidOldPassword("qqqq1111");
        assertThat(result).isTrue();

    }

}