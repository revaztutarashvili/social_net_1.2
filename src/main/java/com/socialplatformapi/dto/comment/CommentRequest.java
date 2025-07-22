package com.socialplatformapi.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

    @NotNull(message = "Post ID must not be null")
    private Long postId;

    @NotBlank(message = "Comment text must not be blank")
    @Size(min = 2, max = 128)
    private String text;
}
