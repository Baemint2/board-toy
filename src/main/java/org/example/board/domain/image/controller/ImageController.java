package org.example.board.domain.image.controller;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.image.dto.ImageUploadDto;
import org.example.board.domain.image.service.ImageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public String upload(@ModelAttribute ImageUploadDto imageUploadDto,  Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        imageService.uploadProfile(imageUploadDto, userDetails.getUsername());

        return "redirect:/user/info";
    }
}
