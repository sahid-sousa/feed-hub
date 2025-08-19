package br.com.feedhub.interfaces.controllers.comment;

import br.com.feedhub.application.usecases.comment.CreateComment;
import br.com.feedhub.application.usecases.comment.UpdateComment;
import br.com.feedhub.interfaces.dto.request.comment.CommentCreateRequest;
import br.com.feedhub.interfaces.dto.request.comment.CommentUpdateRequest;
import br.com.feedhub.interfaces.dto.response.CommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment Feedback Endpoint")
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CreateComment createComment;
    private final UpdateComment updateComment;

    public CommentController(CreateComment createComment, UpdateComment updateComment) {
        this.createComment = createComment;
        this.updateComment = updateComment;
    }

    @Operation(
            summary = "Add new Comment",
            description = "Adds new Comment by passing a JSON",
            security = { @SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(
                            description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CommentResponse.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)

            }
    )
    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody CommentCreateRequest commentCreateRequest, HttpServletRequest request) {
        var commentCreated = createComment.execute(commentCreateRequest, request);
        return ResponseEntity.ok().body(commentCreated);
    }

    @Operation(
            summary = "Update a Comment",
            description = "Updates a Comment by passing JSON",
            security = { @SecurityRequirement(name = "bearerAuth") },
            responses = {
                    @ApiResponse(
                            description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CommentResponse.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody CommentUpdateRequest commentUpdateRequest, HttpServletRequest request) {
        var commentUpdated = updateComment.execute(commentUpdateRequest, request);
        return ResponseEntity.ok().body(commentUpdated);
    }


}
