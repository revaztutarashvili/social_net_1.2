package com.socialplatformapi.controller;

import com.socialplatformapi.dto.comment.CommentRequest;
import com.socialplatformapi.dto.comment.CommentResponse;
import com.socialplatformapi.dto.comment.CommentSummary;
import com.socialplatformapi.dto.comment.CommentUpdateRequest;
import com.socialplatformapi.service.AuthorizationService;
import com.socialplatformapi.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final AuthorizationService authorizationService;

    @Operation(
            summary = "Add comment",
            parameters = {
                    @Parameter(name = "X-Session-Token", in = ParameterIn.HEADER, required = true, description = "Session token")
            }
    )
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @RequestBody @Valid CommentRequest request,
            HttpServletRequest httpRequest) {

        var user = authorizationService.getLoggedInUser(httpRequest);
        var response = commentService.addComment(request, user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update comment",
            parameters = {
                    @Parameter(name = "X-Session-Token", in = ParameterIn.HEADER, required = true)
            }
    )
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable(name = "id") Long commentId,
            @RequestBody @Valid CommentUpdateRequest request,
            HttpServletRequest httpRequest
    ) {
        var user = authorizationService.getLoggedInUser(httpRequest);
        var response = commentService.updateComment(commentId, request, user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete comment",
            parameters = {
                    @Parameter(name = "X-Session-Token", in = ParameterIn.HEADER, required = true)
            }
    )
    public ResponseEntity<String> deleteComment(
            @PathVariable(name = "id") Long commentId,
            HttpServletRequest httpRequest
    ) {
        var user = authorizationService.getLoggedInUser(httpRequest);
        commentService.deleteComment(commentId, user);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    @GetMapping("/by-post/{postId}")
    public List<CommentSummary> getCommentsByPost(@PathVariable Long postId,
                                                  @RequestParam(defaultValue = "0") int page) {
        return commentService.getCommentsByPost(postId, PageRequest.of(page, 10));
    }

    @GetMapping("/by-user/{username}")
    public List<CommentResponse> getCommentsByUser(@PathVariable String username,
                                                   @RequestParam(defaultValue = "0") int page) {
        return commentService.getCommentsByUser(username, PageRequest.of(page, 10));
    }

}
