package org.example.board.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.image.dto.ImageResponseDto;
import org.example.board.domain.image.service.ImageService;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.user.service.UserService;
import org.example.board.domain.user.dto.UserCreateDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ImageService imageService;
    @GetMapping("/signup")
    public String signup(UserCreateDto userCreateDto) {
        return "signup_form";
    }


    @PostMapping("/signup")
    public String signup(@Valid UserCreateDto userCreateDto,
                         BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("userCreateDto", userCreateDto);
            return "signup_form";
        }
        if(!userCreateDto.getPassword1().equals(userCreateDto.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            log.info("비밀 번호 불일치");
            return "signup_form";
        }
        try{
            Long siteUser = userService.create(userCreateDto);
            log.info("createUser = {}", siteUser);
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            log.info("이미 등록된 사용자입니다.", e);
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }


    @GetMapping("/info")
    public String userInfo(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        SiteUser siteUser = userService.findUserIdByUsername(userDetails.getUsername());
        ImageResponseDto image = imageService.findImage(userDetails.getUsername());

        model.addAttribute("siteUser", siteUser);
        model.addAttribute("image", image);

        return "/member/info";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

}
