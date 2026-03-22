package com.udonmarket.backend.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<LocationDto> getUserLocations(Long userId) {
        return locationRepository.findByUserId(userId).stream()
                .map(loc -> LocationDto.builder()
                        .name(loc.getBaseAddress())
                        .sido(loc.getSido())              // 시/도 반환 ← 수정
                        .sigungu(loc.getSigungu())        // 시/군/구 반환 ← 추가
                        .dong(loc.getDong())              // 행정동 반환
                        .legalDong(loc.getLegalDongCode()) // 법정동 반환 ← 추가
                        .roadName(loc.getRoadName())
                        .zipCode(loc.getZipCode())
                        .lat(loc.getLat())
                        .lon(loc.getLon())
                        .verified(loc.isVerified())
                        .expired(loc.isExpired())
                        .selected(loc.isSelected())
                        .authDate(loc.getAuthDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveUserLocations(Long userId, List<LocationDto> dtos) {
        locationRepository.deleteByUserId(userId);
        List<Location> locations = dtos.stream()
                .map(dto -> Location.builder()
                        .userId(userId)
                        .zipCode(dto.getZipCode() != null ? dto.getZipCode() : "00000")
                        .baseAddress(dto.getName() != null ? dto.getName() : "")
                        .sido(dto.getSido() != null ? dto.getSido() : "")           // 시/도 저장 ← 추가
                        .sigungu(dto.getSigungu() != null ? dto.getSigungu() : "")  // 시/군/구 저장 ← 추가
                        .legalDongCode(dto.getLegalDong() != null ? dto.getLegalDong() : "")  // 법정동 저장
                        .dong(dto.getDong())              // 행정동 저장
                        .roadName(dto.getRoadName())
                        .lat(dto.getLat())
                        .lon(dto.getLon())
                        .verified(dto.isVerified())
                        .expired(dto.isExpired())
                        .selected(dto.isSelected())
                        .authDate(dto.getAuthDate())
                        .build())
                .collect(Collectors.toList());
        locationRepository.saveAll(locations);
    }

    public List<LocationDto> findUserLocations(Long userId) {
        List<Location> locations = locationRepository.findByUserId(userId);

        // 엔티티 리스트에서 하나씩 꺼내 DTO로 변환
        return locations.stream()
                .map(location -> {
                        LocationDto dto = new LocationDto();
                        dto.setSido(location.getSido());
                        dto.setSigungu(location.getSigungu());
                        dto.setLegalDong(location.getLegalDongCode());

                        return dto;
                    }
                )
                .toList();
    }
}