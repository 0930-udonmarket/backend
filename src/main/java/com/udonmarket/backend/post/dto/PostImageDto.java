package com.udonmarket.backend.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
public class PostImageDto {

    private List<MultipartFile> files;
}
