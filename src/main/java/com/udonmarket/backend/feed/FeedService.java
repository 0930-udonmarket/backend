package com.udonmarket.backend.feed;

import com.udonmarket.backend.post.Post;
import com.udonmarket.backend.post.PostDto;
import com.udonmarket.backend.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;

    /**
     * init-data: { products: [...], requests: [] }
     * main.js가 기대하는 구조 그대로 반환
     */
    public Map<String, Object> getInitData() {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        List<PostDto> products = posts.stream()
                .map(this::toFeedDto)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("products", products);
        result.put("requests", new ArrayList<>());  // 요청글은 나중에 구현
        return result;
    }

    /**
     * feed: 필터/정렬 적용한 공구 목록 반환
     */
    public List<PostDto> getFeed(String viewMode, String category,
                                 String keyword, String sort,
                                 Double lat, Double lon) {

        // 요청글 탭은 일단 빈 배열
        if ("requests".equals(viewMode)) return new ArrayList<>();

        List<Post> posts;

        // 키워드 검색
        if (keyword != null && !keyword.isEmpty()) {
            posts = postRepository.searchByKeyword(keyword);
        }
        // 카테고리 필터
        else if (category != null && !"all".equals(category)) {
            posts = postRepository.findByCategoryCodeOrderByCreatedAtDesc(category);
        }
        // 전체
        else {
            posts = postRepository.findAllByOrderByCreatedAtDesc();
        }

        List<PostDto> dtos = posts.stream()
                .map(this::toFeedDto)
                .collect(Collectors.toList());

        // 거리순 정렬
        if ("distance".equals(sort) && lat != null && lon != null) {
            final double userLat = lat, userLon = lon;
            dtos.forEach(dto -> {
                if (dto.getLat() != null && dto.getLon() != null) {
                    int dist = calculateDistance(userLat, userLon,
                            dto.getLat(), dto.getLon());
                    // distance 필드는 PostDto에 있으므로 rebuild 필요
                }
            });
            dtos.sort(Comparator.comparingInt(d ->
                    d.getDistance() != null ? d.getDistance() : 99999));
        }

        return dtos;
    }

    // Post → main.js 카드가 기대하는 형태의 Dto 변환
    private PostDto toFeedDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .title(post.getTitle())
                .content(post.getContent())
                .targetCount(post.getTargetCount() != null ? post.getTargetCount() : 1)
                .currentCount(post.getCurrentCount() != null ? post.getCurrentCount() : 0)
                .viewCount(post.getViewCount() != null ? post.getViewCount() : 0)
                .deadline(post.getDeadline())
                .status(post.getStatus() != null ? post.getStatus() : "OPEN")
                .createdAt(post.getCreatedAt())
                .categoryCode(post.getCategoryCode())
                .tags(post.getTags())
                .pricePerPerson(post.getPricePerPerson() != null ? post.getPricePerPerson() : 0L)
                .image(post.getImage())          // DB image 컬럼 (로컬 이미지 경로)
                .location(post.getLocation())    // DB location 컬럼 (dong 이름)
                .shopName("인증된 가게")
                .build();
    }

    private int calculateDistance(double lat1, double lon1,
                                  double lat2, double lon2) {
        final int R = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return (int) (R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }
}