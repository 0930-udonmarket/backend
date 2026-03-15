package com.udonmarket.backend.review.repository;

import com.udonmarket.backend.review.entity.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewRepository { // review 엔티티를 저장하고 관리
    @PersistenceContext
    EntityManager em;

    public void save(Review review) {
        em.persist(review);
    }

    public Review findOne(Long id) {
        return em.find(Review.class, id);
    }

    public List<Review> findAll() {
        return em.createQuery("SELECT r FROM Review r", Review.class).getResultList();

    }

    public List<Review> findByUserId(Long userId) {
        return em.createQuery("SELECT r FROM Review r WHERE r.userId = :userId", Review.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
