package com.socialplatformapi.dto.comment;

import com.socialplatformapi.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String text;
    private String commenterUsername;
    private Long postId;
    private LocalDateTime commentDate;

    public static CommentResponse commentToDto(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getCommentText(),
                comment.getCommenter().getUsername(),
                comment.getPost().getId(),
                comment.getCommentDate()
        );
    }
}
