package com.udonmarket.backend.region;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    public List<String> getSidoList() {
        return regionRepository.findDistinctSido();
    }

    public List<String> getSigunguList(String sido) {
        return regionRepository.findSigunguBySido(sido);
    }

    public List<String> getDongList(String sido, String sigungu) {
        return regionRepository.findDongBySidoAndSigungu(sido, sigungu);
    }
}