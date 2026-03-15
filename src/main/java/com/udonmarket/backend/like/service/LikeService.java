package com.udonmarket.backend.like.service;

import com.udonmarket.backend.like.entity.Like;
import com.udonmarket.backend.like.repository.LikeRepository;
import com.udonmarket.backend.post.entity.Post;
import com.udonmarket.backend.post.repository.PostRepository;
import com.udonmarket.backend.user.entity.User;
import com.udonmarket.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor // @Autowired 대신 생성자로 주입받음
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 찜 생성 메소드
    @Transactional
    public Long addLike(Long userId, Long postId) {

        // User와 Post 객체를 DB에서 찾아옴
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 찜 객체 생성 몇 연결
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);

        // 저장
        likeRepository.save(like);

        // 생성된 Id 반환
        return like.getId();
    }

    // 찜 취소 메소드
    @Transactional
    public void cancelLike(Long likeId) {
        likeRepository.deleteById(likeId);
    }

    // 찜 전체 조회 메소드
    public List<Like> findByUserId(Long userId) {
        return likeRepository.findByUserId(userId);
    }
}
