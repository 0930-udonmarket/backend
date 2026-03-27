package com.udonmarket.backend.order.service;

import com.udonmarket.backend.order.dto.OrderDto;
import com.udonmarket.backend.order.entity.Order;
import com.udonmarket.backend.order.repository.OrderRepository;
import com.udonmarket.backend.post.Post;
import com.udonmarket.backend.post.PostRepository;
import com.udonmarket.backend.product.entity.Product;
import com.udonmarket.backend.user.entity.User;
import com.udonmarket.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // 레포지토리 자동 주입
public class OrderService {

    private final OrderRepository orderRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 공동 구매 참여 시 디비 정보 변경 함수
    @Transactional
    public void joinGroupBuy(Long userId, Long postId, int quantity) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글(Id: " + postId + ")이 없습니다."));

        // 본인 게시글 참여 방지 ← 추가
        if (post.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인이 등록한 공동구매에는 참여할 수 없습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저(Id: " + userId + ")이 없습니다."));

        // Order 정보 생성 및 저장
        Order order = new Order();
        order.setUser(user);
        order.setPost(post);

        order.setPartStatus("ACTIVE");
        order.setQuantity(quantity);
        order.setJoinedAt(LocalDateTime.now());

        orderRepository.save(order); // 저장

        // Post의 현재 인원 수 업데이트
        int currentCount = (post.getCurrentCount() == null ? 0 : post.getCurrentCount()) + 1;
        post.setCurrentCount(currentCount);

        // Post의 1인당 부담금 계산 및 업데이트
        Product product = post.getProduct();
        if (product != null && currentCount > 0) {
            Long totalPrice = product.getSalePrice();
            Long pricePerPerson = totalPrice / currentCount;

            post.setPricePerPerson(pricePerPerson);
        }

        // 변경사항은 DB에 자동 반영
        postRepository.save(post);

    }

    @Transactional
    public List<OrderDto> getOrders(Long userId, String status) {

        // 디비에서 가져오기
        List<Order> order = orderRepository.findUserOrderByStatus(userId, status);

        return order.stream()
                .map(o -> {
                    Post post = o.getPost();
                    Product product = post.getProduct();
                    Long pricePerPerson = post.getPricePerPerson();

                    // userId로 판매자 정보 조회
                    User seller = userRepository.findById(post.getUserId())
                            .orElse(null);
                    String sellerName = "알 수 없음";
                    if (seller != null) {
                        sellerName = seller.getNickname() != null
                                ? seller.getNickname()
                                : seller.getName();
                    }

                    // 이미지 가져오기 (post_image 테이블에서 첫 번째 이미지)
                    String imageUrl = null;
                    if (post.getImages() != null && !post.getImages().isEmpty()) {
                        imageUrl = post.getImages().get(0).getUrl();
                    }

                    return new OrderDto(
                            o.getId(),
                            post.getId(),
                            post.getTitle(),
                            sellerName,
                            post.getCreatedAt(),
                            post.getStatus(),
                            o.getPartStatus(),
                            pricePerPerson,
                            imageUrl  // ← post.getImage() 대신 post_image에서 가져온 url
                    );
                })
                .collect(Collectors.toList());
    }


}