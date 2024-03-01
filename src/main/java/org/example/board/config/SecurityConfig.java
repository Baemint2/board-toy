package org.example.board.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/css/**", "/js/**","/img/**", "/font/**");
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.
                cors(AbstractHttpConfigurer::disable)
                .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(config ->
                        config
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/posts/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/user/login")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/user/signup")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/user/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/posts/detail/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/posts/images/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/email/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/posts/**")).hasRole("USER")
                                .requestMatchers(new AntPathRequestMatcher("/api/**/**")).hasRole("USER")
                                .requestMatchers(new AntPathRequestMatcher("/user/**")).hasRole("USER")
                                .anyRequest().permitAll())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/api/v1/**"))
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/api/email/**")))
                .formLogin(login -> login
                        .loginPage("/user/login")
                        .successHandler(new CustomLoginSuccessHandler()))
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
