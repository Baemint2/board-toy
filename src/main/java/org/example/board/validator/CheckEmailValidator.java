package org.example.board.validator;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.user.UserRepository;
import org.example.board.domain.user.dto.UserCreateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class CheckEmailValidator extends AbstractValidator<UserCreateDto> {

    private final UserRepository userRepository;
    @Override
    protected void doValidate(UserCreateDto createDto, Errors errors) {
        if(userRepository.existsByEmail(createDto.getEmail())) {
            errors.rejectValue("email", "email.duplicate", "이미 사용중인 이메일 입니다.");
        }
    }
}
