package org.example.board.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.board.config.DataNotFoundException;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return user;
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
}
