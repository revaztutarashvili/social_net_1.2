package com.socialplatformapi.service;

import com.socialplatformapi.dto.comment.CommentResponse;
import com.socialplatformapi.dto.comment.CommentSummary;
import com.socialplatformapi.dto.post.PostRequest;
import com.socialplatformapi.dto.post.PostResponse;
import com.socialplatformapi.exception.post.PostException;
import com.socialplatformapi.model.Post;
import com.socialplatformapi.model.User;
import com.socialplatformapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentService commentService;
    private final LikeService likeService;

    public PostResponse createPost(PostRequest request, User user) {
        Post post = new Post();
        post.setText(request.getText());
        post.setPostDate(LocalDateTime.now());
        post.setPoster(user);
        Post savedPost = postRepository.save(post);

        return PostResponse.newPostToDto(savedPost);
    }

    public PostResponse updatePost(Long postId,PostRequest request, User user) {
        Post post = getPost(postId, user);

        post.setText(request.getText());
        post.setPostDate(LocalDateTime.now());
        Post savedPost = postRepository.save(post);

        List<CommentSummary> comments = commentService
                .getCommentsByPost(savedPost.getId(), PageRequest.of(0,10));

        List<String> likedBy = likeService
                .getUsernamesWhoLiked(savedPost.getId(), PageRequest.of(0,10));

        return PostResponse.postToDto(savedPost, comments, likedBy);
    }

    public void deletePost(Long postId, User user) {
        Post post = getPost(postId, user);
        postRepository.delete(post);
    }

    public Post getPost(Long postId, User user) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new PostException("Post with id " + postId + " does not exist");
        }
        Post post = postOptional.get();
        if (!post.getPoster().getId().equals(user.getId())) {
            throw new PostException("You are not the author of this post");
        }
        return post;
    }

    public PostResponse getPostResponse(Long postId, Pageable commentPageable, Pageable likesPageable) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new PostException("Post with id " + postId + " does not exist");
        }
        Post post = postOptional.get();

        List<CommentSummary> comments = commentService
                .getCommentsByPost(post.getId(), commentPageable);

        List<String> likedBy = likeService
                .getUsernamesWhoLiked(post.getId(), likesPageable);

        return PostResponse.postToDto(post, comments, likedBy);
    }

    public List<PostResponse> getAllPosts(Pageable pageable) {
        List<Post> postList = postRepository
                .findAllBy(pageable)
                .stream()
                .toList();

        return getPostResponses(postList);
    }

    public List<PostResponse> getPostsByUser(String username, Pageable pageable) {
        List<Post> postList = postRepository
                .findAllByPosterUsername(username, pageable)
                .stream()
                .toList();

        return getPostResponses(postList);
    }

    public List<PostResponse> getPostResponses(List<Post> postList) {
        List<PostResponse> postResponseList = new ArrayList<>();
        for (Post post : postList) {
            List<CommentSummary> comments =
                    commentService.getCommentsByPost(post.getId(), PageRequest.of(0, 5));
            List<String> likedBy =
                    likeService.getUsernamesWhoLiked(post.getId(), PageRequest.of(0,5));
            postResponseList.add(PostResponse.postToDto(post, comments, likedBy));
        }
        return postResponseList;
    }
}
