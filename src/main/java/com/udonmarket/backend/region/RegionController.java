package com.udonmarket.backend.region;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    /** GET /api/v1/regions/sido */
    @GetMapping("/sido")
    public ResponseEntity<List<String>> getSidoList() {
        return ResponseEntity.ok(regionService.getSidoList());
    }

    /** GET /api/v1/regions/sigungu?sido= */
    @GetMapping("/sigungu")
    public ResponseEntity<List<String>> getSigunguList(@RequestParam String sido) {
        return ResponseEntity.ok(regionService.getSigunguList(sido));
    }

    /** GET /api/v1/regions/dong?sido=&sigungu= */
    @GetMapping("/dong")
    public ResponseEntity<List<String>> getDongList(
            @RequestParam String sido,
            @RequestParam String sigungu) {
        return ResponseEntity.ok(regionService.getDongList(sido, sigungu));
    }
}