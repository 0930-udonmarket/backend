package com.udonmarket.backend.post.repository;

import com.udonmarket.backend.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
