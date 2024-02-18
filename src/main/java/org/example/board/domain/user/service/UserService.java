package org.example.board.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.board.config.DataNotFoundException;
import org.example.board.domain.image.Image;
import org.example.board.domain.image.ImageRepository;
import org.example.board.domain.user.Role;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.user.UserRepository;
import org.example.board.domain.user.dto.UserCreateDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long create(UserCreateDto createDto) {
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

    public SiteUser findUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
    }
}
