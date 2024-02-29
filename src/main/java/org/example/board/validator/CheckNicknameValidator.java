package org.example.board.validator;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.user.repository.UserRepository;
import org.example.board.domain.user.dto.UserCreateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class CheckNicknameValidator extends AbstractValidator<UserCreateDto> {

    private final UserRepository userRepository;
    @Override
    protected void doValidate(UserCreateDto createDto, Errors errors) {
        if(userRepository.existsByNickname(createDto.getNickname())) {
            errors.rejectValue("nickname", "nickname.duplicate");
        }
    }
}
