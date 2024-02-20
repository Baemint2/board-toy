package org.example.board.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.config.DataNotFoundException;
import org.example.board.domain.answer.AnswerRepository;
import org.example.board.domain.image.Image;
import org.example.board.domain.image.ImageRepository;
import org.example.board.domain.user.Role;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.user.UserRepository;
import org.example.board.domain.user.dto.UserCreateDto;
import org.example.board.exception.CustomException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final AnswerRepository answerRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long create(UserCreateDto createDto) {

        // 비밀번호 일치 검사
        if(!createDto.getPassword1().equals(createDto.getPassword2())) {
            throw new CustomException("password2","비밀번호가 일치하지 않습니다.");
        }
        // 사용자명 중복 검사
        if (userRepository.existsByUsername(createDto.getUsername())) {
            throw new CustomException("username","이미 사용중인 사용자명입니다.");
        }

        // 이메일 중복 검사
        if (userRepository.existsByEmail(createDto.getEmail())) {
            throw new CustomException("email","이미 등록된 이메일입니다.");
        }


        SiteUser user = SiteUser.builder()
                .username(createDto.getUsername())
                .email(createDto.getEmail())
                .password(passwordEncoder.encode(createDto.getPassword1()))
                .role(Role.USER)
                .build();

        Image image = Image.builder()
                .url("/image/anonymous.png")
                .siteUser(user)
                .build();

        userRepository.save(user);
        imageRepository.save(image);
        return user.getId();
    }

    @Transactional
    public SiteUser getUser(String userName) {
        Optional<SiteUser> user = userRepository.findByUsername(userName);
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new DataNotFoundException("user not found");
        }
    }

    @Transactional
    public boolean deleteUser(String username, String password) {
        Optional<SiteUser> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            SiteUser siteUser = userOptional.get();
            log.info("유저 찾기 = {}", siteUser.getUsername());
            if (passwordEncoder.matches(password, siteUser.getPassword())) {

                answerRepository.deleteBySiteUserId(siteUser.getId());
                imageRepository.deleteBySiteUserId(siteUser.getId());
                userRepository.deleteById(siteUser.getId());
                log.info("password matches = {}", username);
                return true;
            }
        }
        return false;
    }

    public SiteUser findUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
    }

    public boolean checkUsernameDuplicate(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }
}
