package com.udonmarket.backend.review.controller;

import com.udonmarket.backend.review.dto.ReviewDto;
import com.udonmarket.backend.review.dto.ReviewImageDto;
import com.udonmarket.backend.review.entity.Review;
import com.udonmarket.backend.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// @CrossOrigin(origins = "*")
@RestController
@RequestMapping("/review") // '/api/review'로 들어오는 모든 요청을 해당 컨트롤러가 처리
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 리뷰 생성
    @PostMapping("/insert") // /api/review/insert
    public ResponseEntity<?> createReview(@ModelAttribute ReviewDto reviewDto, @ModelAttribute ReviewImageDto reviewImageDto) {
        // Long res = reviewService.insertReview(review);

        Long res = reviewService.insertReview(reviewDto, reviewImageDto);

//        System.out.println("들어온 유저ID: " + review.getUserId());
//        System.out.println("들어온 상품ID: " + review.getProductId())

        if (res != null) {
            return ResponseEntity.status(200).body("등록 성공");

        } else {
            return ResponseEntity.status(400).body("등록 실패");
        }
    }

    // 리뷰 전체 조회
    @GetMapping("/list/{userId}")
    public List<?> getAllReviewsByUserId(@PathVariable("userId") Long userId) {
        List<Review> reviews = reviewService.findByUserId(userId);

        return reviews.stream()
                .map(ReviewDto::new) // ReviewDto 생성자 사용
                .collect(Collectors.toList());

    }

    // 리뷰 상세 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReviewByReviewId(@PathVariable("reviewId") Long reviewId) {
        Review review = reviewService.findOne(reviewId);

        if (review == null) {
            return ResponseEntity.status(404).body("해당 리뷰를 찾을 수 없습니다.");
        }

        // 엔티티를 DTO로 변환해서 반환
        ReviewDto reviewDto = new ReviewDto(review);
        return ResponseEntity.ok(reviewDto);
    }

}
