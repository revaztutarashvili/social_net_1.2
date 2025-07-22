package com.socialplatformapi.dto.post;

import com.socialplatformapi.dto.comment.CommentResponse;
import com.socialplatformapi.dto.comment.CommentSummary;
import com.socialplatformapi.model.Like;
import com.socialplatformapi.model.Post;
import com.socialplatformapi.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class PostResponse {
    private Long postId;
    private String text;
    private LocalDateTime postDate;
    private String authorUsername;
    private List<CommentSummary> comments;
    private List<String> likedBy;

    public static PostResponse newPostToDto(Post post) {
        return new PostResponse(
                post.getId(),
                post.getText(),
                post.getPostDate(),
                post.getPoster().getUsername(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public static PostResponse postToDto(Post post,
                                         List<CommentSummary> comments,
                                         List<String> likedBy) {
        return new PostResponse(
                post.getId(),
                post.getText(),
                post.getPostDate(),
                post.getPoster().getUsername(),
                comments,
                likedBy
        );
    }
}
