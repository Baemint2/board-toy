package org.example.board.web.apiController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.board.service.UserService;
import org.example.board.web.dto.user.UserCreateDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/api/v1/sign")
    public Long save(@Valid @RequestBody UserCreateDto userCreateDto) {
        return userService.save(userCreateDto);
    }
}
