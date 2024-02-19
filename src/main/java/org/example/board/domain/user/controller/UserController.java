package org.example.board.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.image.dto.ImageResponseDto;
import org.example.board.domain.image.service.ImageService;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.user.service.UserService;
import org.example.board.domain.user.dto.UserCreateDto;
import org.example.board.validator.CheckEmailValidator;
import org.example.board.validator.CheckUsernameValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final CheckUsernameValidator usernameValidator;
    private final CheckEmailValidator emailValidator;
    private final ImageService imageService;
    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(usernameValidator);
        binder.addValidators(emailValidator);
    }

    @GetMapping("/signup")
    public String signup(UserCreateDto userCreateDto) {
        return "signup_form";
    }



    @PostMapping("/signup")
    public String signup(@Valid UserCreateDto userCreateDto,
                         Errors errors, Model model) {

        if(errors.hasErrors()) {
            model.addAttribute("userCreateDto", userCreateDto);
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "signup_form";
        }
        try{
            Long siteUser = userService.create(userCreateDto);
            log.info("createUser = {}", siteUser);
            return "redirect:/";
        } catch (DataIntegrityViolationException e) {
            errors.reject("signupFailed", "이미 등록된 사용자입니다.");
            log.info("이미 등록된 사용자입니다.", e);
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            errors.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
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
