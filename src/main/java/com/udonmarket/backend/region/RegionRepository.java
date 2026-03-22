package com.udonmarket.backend.region;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<RegionEntity, Long> {

    @Query("SELECT DISTINCT r.sido FROM RegionEntity r ORDER BY r.sido")
    List<String> findDistinctSido();

    @Query("SELECT DISTINCT r.sigungu FROM RegionEntity r WHERE r.sido = :sido ORDER BY r.sigungu")
    List<String> findSigunguBySido(@Param("sido") String sido);

    @Query("SELECT r.dong FROM RegionEntity r WHERE r.sido = :sido AND r.sigungu = :sigungu ORDER BY r.dong")
    List<String> findDongBySidoAndSigungu(@Param("sido") String sido, @Param("sigungu") String sigungu);
}