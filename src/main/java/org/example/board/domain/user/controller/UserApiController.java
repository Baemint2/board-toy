package org.example.board.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.user.dto.NicknameUpdateDto;
import org.example.board.domain.user.dto.UserCreateDto;
import org.example.board.domain.user.dto.UserDeleteDto;
import org.example.board.domain.user.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserApiController {

    private final UserService userService;

    //회원 가입
    @PostMapping("/sign")
    public ResponseEntity<?> join(@Valid @RequestBody UserCreateDto userCreateDto) {
        try {
            Long userId = userService.create(userCreateDto);
            log.info("회원 가입 = {}", userId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "회원가입이 성공적으로 완료되었습니다.", "userId", userId));
        } catch (DataIntegrityViolationException e) {
            log.error("회원 가입 실패 : 데이터 무결성 위반 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("signupFailed", "이미 등록된 사용자입니다."));
        } catch (Exception e) {
            log.error("회원 가입 실패: 예상치 못한 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("signupFailed", "예상치 못한 오류가 발생했습니다."));
        }
    }

    // 아이디 중복 체크
    @GetMapping("/username/check")
    public ResponseEntity<?> checkUsername(@RequestParam("username") String userName) {
        boolean isUserNameDuplicate = userService.checkUsernameDuplicate(userName);
        log.info("중복된 아이디 = {}", isUserNameDuplicate);
        return ResponseEntity.ok(Map.of("isUserNameDuplicate", isUserNameDuplicate));
    }

    // 이메일 중복 체크
    @GetMapping("/email/check")
    public ResponseEntity<?> checkEmail(@RequestParam("email") String email) {
        boolean isEmailDuplicate = userService.checkEmailDuplicate(email);
        log.info("중복된 이메일 = {}", isEmailDuplicate);
        return ResponseEntity.ok(Map.of("isEmailDuplicate", isEmailDuplicate));
    }

    // 닉네임 중복 체크
    @GetMapping("/nickname/check")
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname) {
        boolean isNicknameDuplicate = userService.checkNicknameDuplicate(nickname);
        log.info("중복된 닉네임 = {}", isNicknameDuplicate);
        return ResponseEntity.ok(Map.of("isNickNameDuplicate", isNicknameDuplicate));
    }

    // 회원 탈퇴
    @DeleteMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteUser(@PathVariable String username,
                                        @RequestBody UserDeleteDto dto,
                                        HttpServletRequest request) {
        boolean deleteUser = userService.deleteUser(dto.getUsername(), dto.getPassword());
        if(deleteUser) {
            SecurityContextHolder.clearContext();

            HttpSession session = request.getSession(false);
            if(session != null) {
                session.invalidate();
            }
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 삭제 실패: 사용자명 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    // 닉네임 변경
    @PostMapping("/updateNickname")
    public ResponseEntity<?> updateNickname(@RequestParam("nickname") NicknameUpdateDto nicknameUpdateDto, Principal principal) {
        String username = principal.getName();
        // 중복 닉네임 검사
        if (userService.checkNicknameDuplicate(nicknameUpdateDto.getNickname())) {
            return ResponseEntity.badRequest().body(Map.of("errorMessage", "이미 사용 중인 닉네임입니다."));
        }
        // 닉네임 업데이트
        userService.updateNickName(username, nicknameUpdateDto);
        return ResponseEntity.ok().body(Map.of("redirect", "닉네임이 성공적으로 업데이트되었습니다."));
    }

}
