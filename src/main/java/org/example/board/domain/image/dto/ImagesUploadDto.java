package org.example.board.domain.image.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ImagesUploadDto {

    List<MultipartFile> files;
}
