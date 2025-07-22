package com.socialplatformapi.repository;

import com.socialplatformapi.model.Like;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsLikeByPostIdAndUserId(Long postId, Long userId);
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
    List<Like> findAllByPostId(Long postId, Pageable pageable);
}
