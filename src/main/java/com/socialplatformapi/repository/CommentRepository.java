package com.socialplatformapi.repository;

import com.socialplatformapi.dto.comment.CommentSummary;
import com.socialplatformapi.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<CommentSummary> findAllByPostId(Long postId, Pageable pageable);
    List<Comment> findAllByCommenterUsername(String username, Pageable pageable);
    List<CommentSummary> findAllById(Long id, Pageable pageable);
}
