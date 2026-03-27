package com.udonmarket.backend.review.service;

import com.udonmarket.backend.review.dto.ReviewDto;
import com.udonmarket.backend.review.dto.ReviewImageDto;
import com.udonmarket.backend.review.entity.Review;
import com.udonmarket.backend.review.entity.ReviewImage;
import com.udonmarket.backend.review.repository.ReviewImageRepository;
import com.udonmarket.backend.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewImageRepository reviewImageRepository;

    // 리뷰 생성 메소드
    public Long insertReview(ReviewDto reviewDto, ReviewImageDto reviewImageDto) {

        Review review = Review.builder()
                .userId(reviewDto.getUserId())
                .postId(reviewDto.getPostId())
                .content(reviewDto.getContent())
                .rating(reviewDto.getRating())
                .build();

        if (reviewImageDto.getFiles() != null && !reviewImageDto.getFiles().isEmpty()) {

            // 실제 reviewImages들이 모여있는 내 로컬 폴더 저장소
            String uploadFolder = "D:/koreaIT_java/frontend/front_1900_pkw/portfolio_0930_pkw/udon_market/frontend/src/images/review/";
//            String uploadFolder = System.getProperty("user.dir") + "/src/main/resources/review/";

            File folder = new File(uploadFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }


            System.out.println("파일 개수: " + reviewImageDto.getFiles().size());

            for (MultipartFile file : reviewImageDto.getFiles()) {
                UUID uuid = UUID.randomUUID();
                String imageFileName = uuid + "_" + file.getOriginalFilename();

                File destinationFile = new File(uploadFolder + imageFileName);

                try {
                    file.transferTo(destinationFile);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                ReviewImage image = ReviewImage.builder()
                        .url("../../../images/review/" + imageFileName)
                        .build();

                review.addImage(image);
            }

        }

        reviewRepository.save(review);
        return review.getId();

    }

    // 리뷰 상세 조회 메소드
    public Review findOne(Long id) {
        return reviewRepository.findOne(id);
    }

    // 리뷰 전체 조회 메소드
    public List<Review> findByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findOne(id);

        if (review != null) {
            // 이미지 파일 먼저 삭제
            if (review.getReviewImages() != null) {
                for (ReviewImage image : review.getReviewImages()) {
                    String url = image.getUrl();
                    // "../../../images/review/파일명" 에서 파일명만 추출
                    String fileName = url.substring(url.lastIndexOf("/") + 1);
                    String filePath = "D:/koreaIT_java/frontend/front_1900_pkw/portfolio_0930_pkw/udon_market/frontend/src/images/review/" + fileName;

                    File file = new File(filePath);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
            // DB에서 삭제 (CascadeType.ALL로 review_image도 자동 삭제)
            reviewRepository.deleteById(id);
        }
    }
}