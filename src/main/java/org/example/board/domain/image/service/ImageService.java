package org.example.board.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.image.Image;
import org.example.board.domain.image.ImageRepository;
import org.example.board.domain.image.dto.ImageResponseDto;
import org.example.board.domain.image.dto.ImageUploadDto;
import org.example.board.domain.image.dto.ImagesUploadDto;
import org.example.board.domain.posts.Posts;
import org.example.board.domain.posts.dto.PostsResponseDto;
import org.example.board.domain.posts.repository.PostsRepository;
import org.example.board.domain.user.SiteUser;
import org.example.board.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final PostsRepository postsRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir.profile}")
    private String profileUploadFolder;

    @Value("${file.upload-dir.posts}")
    private String postsUploadFolder;


    // 프로필 업로드
    public void uploadProfile(ImageUploadDto imageUploadDto, String username) {
        SiteUser siteUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("유저네임이 존재하지 않습니다."));
        MultipartFile file = imageUploadDto.getFile();

        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + file.getOriginalFilename();

        File destinationFile = new File(profileUploadFolder + "/" +imageFileName);

        try {
            file.transferTo(destinationFile);

            Image image = imageRepository.findBySiteUser(siteUser);
            if(image != null) {
                image.updateUrl("/profiles/" + imageFileName);
            } else {
                image = Image.builder()
                        .siteUser(siteUser)
                        .url("/profiles/" + imageFileName)
                        .build();
            }
            imageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Todo: 게시판 이미지 업로드
    public void uploadBoard(ImagesUploadDto uploadDto, Long postId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하는 게시글이 없습니다."));
        List<MultipartFile> files= uploadDto.getFiles();

        for (MultipartFile multipartFile : files) {
            UUID uuid = UUID.randomUUID();
            String imageFileName = uuid + "_" + multipartFile.getOriginalFilename();
            File destinationFile = new File(postsUploadFolder + "/" +imageFileName);

            try {
                multipartFile.transferTo(destinationFile);
                Image image = Image.builder()
                        .url("/images/" + imageFileName)
                        .post(posts)
                        .build();
                imageRepository.save(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
