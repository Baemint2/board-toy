package org.example.board.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.config.DataNotFoundException;
import org.example.board.domain.answer.repository.AnswerRepository;
import org.example.board.domain.image.entity.Image;
import org.example.board.domain.image.repository.ImageRepository;
import org.example.board.domain.user.dto.NicknameUpdateDto;
import org.example.board.domain.user.dto.UserCreateDto;
import org.example.board.domain.user.entity.Role;
import org.example.board.domain.user.entity.SiteUser;
import org.example.board.domain.user.repository.UserRepository;
import org.example.board.exception.CustomException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final AnswerRepository answerRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
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

        // 닉네임 중복 검사
        if (userRepository.existsByNickname(createDto.getNickname())) {
            throw new CustomException("nickname", "이미 사용중인 닉네임입니다.");
        }
        // 이메일 중복 검사
        if (userRepository.existsByEmail(createDto.getEmail())) {
            throw new CustomException("email","이미 등록된 이메일입니다.");
        }

        SiteUser user = SiteUser.builder()
                .username(createDto.getUsername())
                .nickname(createDto.getNickname())
                .email(createDto.getEmail())
                .password(passwordEncoder.encode(createDto.getPassword1()))
                .role(Role.USER)
                .build();

        Image image = Image.builder()
                .url("/profiles/anonymous.png")
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

    //회원 탈퇴
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

    @Transactional
    public SiteUser findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
    }

    public boolean checkUsernameDuplicate(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // 비밀번호 확인
    @Transactional
    public boolean checkIfValidOldPassword(String oldPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        log.info("현재 로그인 된 유저 = {}", currentUser);
        SiteUser siteUser = userRepository.findByUsername(currentUser)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return passwordEncoder.matches(oldPassword, siteUser.getPassword());
    }

    // 비밀번호 변경
    @Transactional
    public void changeUserPassword(String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        log.info("현재 로그인 된 유저 = {}", currentUser);
        SiteUser siteUser = userRepository.findByUsername(currentUser)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        String encodedPassword = passwordEncoder.encode(newPassword);
        siteUser.changePassword(encodedPassword);
        log.info("변경된 비밀번호 = {}", encodedPassword);
        userRepository.save(siteUser);
    }

    // 닉네임 변경
    @Transactional
    public void updateNickName(String username, NicknameUpdateDto updateDto) {
        SiteUser siteUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));
        String newNickname = updateDto.getNickname();
        siteUser.updateNickname(newNickname);
        userRepository.save(siteUser);
    }

    //아이디 찾기
    @Transactional
    public String findUsernameByEmail(String email, String nickname) {
        return userRepository.findByEmailAndNickname(email, nickname).orElse(null);
    }


}
