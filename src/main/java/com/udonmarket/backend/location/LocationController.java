package com.udonmarket.backend.location;

import com.udonmarket.backend.config.jwt.JwtUtil;
import com.udonmarket.backend.user.entity.User;
import com.udonmarket.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService  locationService;
    private final JwtUtil          jwtUtil;
    private final UserRepository   userRepository;  // ← 추가

    /** GET /api/v1/users/locations?role= */
    @GetMapping("/locations")
    public ResponseEntity<List<LocationDto>> getLocations(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String role) {

        Long userId = extractUserId(authHeader);
        return ResponseEntity.ok(locationService.getUserLocations(userId));
    }

    /** PUT /api/v1/users/locations?role= */
    @PutMapping("/locations")
    public ResponseEntity<Map<String, Boolean>> saveLocations(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String role,
            @RequestBody List<LocationDto> locations) {

        Long userId = extractUserId(authHeader);
        locationService.saveUserLocations(userId, locations);
        return ResponseEntity.ok(Map.of("success", true));
    }

    private Long extractUserId(String authHeader) {
        String token    = authHeader.replace("Bearer ", "");
        String userName = jwtUtil.getUserName(token);
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다: " + userName));
        return user.getId();  // ← 실제 DB의 user.id 반환
    }

    /** GET /api/v1/users/locations/{userId} */
    @GetMapping("/locations/{userId}")
    public ResponseEntity<List<LocationDto>> getUserLocations(@PathVariable Long userId) {

        List<LocationDto> locations = locationService.findUserLocations(userId);

        return ResponseEntity.ok(locations);
    }

}