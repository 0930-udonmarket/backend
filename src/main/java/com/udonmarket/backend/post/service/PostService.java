package com.udonmarket.backend.post.service;

import com.udonmarket.backend.post.dto.PostDto;
import com.udonmarket.backend.post.dto.PostImageDto;
import com.udonmarket.backend.post.entity.Post;
import com.udonmarket.backend.post.entity.PostImage;
import com.udonmarket.backend.post.repository.PostImageRepository;
import com.udonmarket.backend.post.repository.PostRepository;
import com.udonmarket.backend.product.entity.Product;
import com.udonmarket.backend.product.repository.ProductRepository;
import com.udonmarket.backend.user.entity.User;
import com.udonmarket.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostImageRepository postImageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    // 공동 구매 게시글 생성 메소드
    public Long insertPost(PostDto postDto, PostImageDto postImageDto) {

        User user = userRepository.findById(postDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Product product = productRepository.findById(postDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        Post post = Post.builder()
                .user(user)
                .product(product)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .targetCount(postDto.getTargetCount())
                .currentCount(1L)
                .tags(postDto.getTags())
                .status("OPEN")
                .build();

        if (postImageDto.getFiles() != null && !postImageDto.getFiles().isEmpty()) {

            // 실제 postImages들이 모여있는 내 로컬 폴더 저장소
            String uploadFolder = "C:/udong/upload/postImages/";

            File folder = new File(uploadFolder);
            if(!folder.exists()) {
                folder.mkdirs();
            }

            System.out.println("파일 개수: " + postImageDto.getFiles().size());
            for (MultipartFile file : postImageDto.getFiles()) {
                UUID uuid = UUID.randomUUID();
                String imageFileName = uuid + "_" + file.getOriginalFilename();

                File destinationFile = new File(uploadFolder + imageFileName);

                try {
                    file.transferTo(destinationFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                PostImage image = PostImage.builder()
                        .url("/postImages/" + imageFileName)
                        .build();

                post.addImage(image);
            }
        }


        return postRepository.save(post).getId();

    }

    // 공동 구매 게시글 상세 조회 메소드
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
    }
}
