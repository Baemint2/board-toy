package org.example.board.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.image.dto.ImageResponseDto;
import org.example.board.domain.image.service.ImageService;
import org.example.board.domain.user.dto.PasswordResetDto;
import org.example.board.domain.user.dto.UserCreateDto;
import org.example.board.domain.user.dto.UserResponseDto;
import org.example.board.domain.user.entity.SiteUser;
import org.example.board.domain.user.service.UserService;
import org.example.board.validator.CheckEmailValidator;
import org.example.board.validator.CheckNicknameValidator;
import org.example.board.validator.CheckUsernameValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final CheckUsernameValidator usernameValidator;
    private final CheckEmailValidator emailValidator;
    private final CheckNicknameValidator nicknameValidator;
    private final ImageService imageService;
    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(usernameValidator);
        binder.addValidators(emailValidator);
        binder.addValidators(nicknameValidator);
    }

    // 회원가입 화면
    @GetMapping("/signup")
    public String signup(Model model,UserCreateDto userCreateDto) {
        model.addAttribute("userCreateDto", userCreateDto);
        return "user/signup-form";
    }

    // 유저 My Page
    @GetMapping("/info")
    public String userInfo(Model model, Principal principal) {
        SiteUser siteUser = userService.findByUsername(principal.getName());
        ImageResponseDto image = imageService.findImage(siteUser.getUsername());

        model.addAttribute("siteUser", siteUser);
        model.addAttribute("image", image);

        return "user/info";
    }

    // 아이디 찾기
    @GetMapping("/find/username")
    public String userInfoUpdate(Model model, UserResponseDto userResponseDto) {
        model.addAttribute("userResponseDto", userResponseDto);
        return "user/find-username";
    }

    // 로그인
    @GetMapping("/login")
    public String login(@RequestParam(value = "redirect", required = false) String redirectUrl, Model model) {
        model.addAttribute("redirectUrl", redirectUrl);
        return "user/login-form";
    }

    // 비밀번호 변경
    @GetMapping("/password/reset")
    public String resetPassword(Model model, PasswordResetDto passwordResetDto) {
        model.addAttribute("passwordResetDto", passwordResetDto);
        return "user/password-reset";
    }

    // 비밀번호 찾기 -> 1. 아이디 입력
    @GetMapping("/find/password")
    public String findPassword(Model model, UserResponseDto userResponseDto) {
        model.addAttribute("userResponseDto", userResponseDto);
        return "user/find-password";
    }

    // 비밀번호 찾기 -> 2. 이메일 인증


}
