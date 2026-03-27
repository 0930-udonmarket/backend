package com.udonmarket.backend.feed;

import com.udonmarket.backend.post.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    /**
     * GET /api/v1/init-data
     * main.js의 MockAPI.init()이 호출 → { products: [...], requests: [...] } 반환
     */
    @GetMapping("/init-data")
    public ResponseEntity<Map<String, Object>> getInitData() {
        return ResponseEntity.ok(feedService.getInitData());
    }

    /**
     * GET /api/v1/feed?viewMode=products&category=all&keyword=&sort=latest&lat=&lon=
     * main.js의 MockAPI.fetchFeed()가 호출
     */
    @GetMapping("/feed")
    public ResponseEntity<List<PostDto>> getFeed(
            @RequestParam(defaultValue = "products") String viewMode,
            @RequestParam(defaultValue = "all") String category,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(required = false) String lat,
            @RequestParam(required = false) String lon) {

        // 프론트에서 "null" 문자열로 넘어오는 경우 처리
        Double latVal = (lat == null || lat.equals("null") || lat.isEmpty()) ? null : Double.parseDouble(lat);
        Double lonVal = (lon == null || lon.equals("null") || lon.isEmpty()) ? null : Double.parseDouble(lon);

        return ResponseEntity.ok(
                feedService.getFeed(viewMode, category, keyword, sort, latVal, lonVal));
    }
}