package com.socialplatformapi.service;

import com.socialplatformapi.exception.like.LikeException;
import com.socialplatformapi.exception.post.PostException;
import com.socialplatformapi.model.Like;
import com.socialplatformapi.model.Post;
import com.socialplatformapi.model.User;
import com.socialplatformapi.repository.LikeRepository;
import com.socialplatformapi.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public void likePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post with id " + postId + " does not exist"));

        if (likeRepository.existsLikeByPostIdAndUserId(post.getId(), user.getId())) {
            throw new LikeException("Post with id " + post.getId() + " is already liked by user " + user.getUsername());
        }

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        like.setLikedAt(LocalDateTime.now());
        likeRepository.save(like);
    }

    public void unlikePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post with id " + postId + " does not exist"));

        Like like = likeRepository.findByUserIdAndPostId(user.getId(), post.getId())
                .orElseThrow(() ->
                        new LikeException("Post with id " + post.getId() + " is not liked by user " + user.getUsername()));

        likeRepository.delete(like);
    }

    public List<String> getUsernamesWhoLiked(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post with id " + postId + " does not exist"));

        return likeRepository.findAllByPostId(post.getId(), pageable)
                .stream()
                .map(l -> l.getUser().getUsername())
                .toList();
    }
}
