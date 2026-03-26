package com.udonmarket.backend.review.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
public class ReviewImageDto {

    private List<MultipartFile> files;
}