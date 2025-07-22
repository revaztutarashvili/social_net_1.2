package com.socialplatformapi.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequest {
    @NotBlank(message = "Text must not be blank")
    private String text;
}
