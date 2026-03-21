package com.udonmarket.backend.region;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "region")
@Getter
public class RegionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sido;      // 시/도

    @Column(nullable = false)
    private String sigungu;   // 시/군/구

    @Column(nullable = false)
    private String dong;      // 읍/면/동
}