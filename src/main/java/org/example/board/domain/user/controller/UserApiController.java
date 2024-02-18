package org.example.board.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.user.service.UserService;
import org.example.board.domain.user.dto.UserCreateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/api/v1/sign")
    public ResponseEntity<Long> join(@Valid @RequestBody UserCreateDto userCreateDto) {
        Long userId = userService.create(userCreateDto);
        return ResponseEntity.ok(userId);
    }
}
