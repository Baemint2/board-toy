package org.example.board.validator;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.user.repository.UserRepository;
import org.example.board.domain.user.dto.NicknameUpdateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class CheckUpdateNicknameValidator extends AbstractValidator<NicknameUpdateDto> {

    private final UserRepository userRepository;
    @Override
    protected void doValidate(NicknameUpdateDto nicknameUpdateDto, Errors errors) {
        if(userRepository.existsByNickname(nicknameUpdateDto.getNickname())) {
            errors.rejectValue("nickname", "nickname.exists", "이미 존재하는 닉네임입니다.");
        }
    }
}
