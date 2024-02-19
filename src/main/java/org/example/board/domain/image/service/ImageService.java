package org.example.board.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.image.Image;
import org.example.board.domain.image.ImageRepository;
import org.example.board.domain.image.dto.ImageResponseDto;
import org.example.board.domain.image.dto.ImageUploadDto;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Value("${file.dir}")
    private String uploadFolder;

    public void upload(ImageUploadDto imageUploadDto, String username) {
        SiteUser siteUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("유저네임이 존재하지 않습니다."));
        MultipartFile file = imageUploadDto.getFile();

        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + file.getOriginalFilename();

        File destinationFile = new File(uploadFolder + "/" +imageFileName);

        try {
            file.transferTo(destinationFile);

            Image image = imageRepository.findBySiteUser(siteUser);
            if(image != null) {
                image.updateUrl("/image/" + imageFileName);
            } else {
                image = Image.builder()
                        .siteUser(siteUser)
                        .url("/image/" + imageFileName)
                        .build();
            }
            imageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageResponseDto findImage(String username) {
        SiteUser siteuser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("유저네임이 존재하지 않습니다."));
        Image image = imageRepository.findBySiteUser(siteuser);

        String defaultImageUrl = "/image/anonymous.png";

        if(image == null) {
            return ImageResponseDto.builder()
                    .url(defaultImageUrl)
                    .build();
        } else {
            return ImageResponseDto.builder()
                    .url(image.getUrl())
                    .build();
        }
    }
}
