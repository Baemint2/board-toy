package org.example.board.validator;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.user.UserRepository;
import org.example.board.domain.user.dto.UserCreateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class CheckUsernameValidator extends AbstractValidator<UserCreateDto> {

    private final UserRepository userRepository;

    @Override
    protected void doValidate(UserCreateDto dto, Errors errors) {
        if(userRepository.existsByUsername(dto.getUsername())) {
            errors.rejectValue("username", "username.duplicate", "이미 사용중인 사용자명입니다.");
        }
    }

}
