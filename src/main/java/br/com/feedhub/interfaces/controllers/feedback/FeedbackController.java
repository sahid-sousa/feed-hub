package br.com.feedhub.interfaces.controllers.feedback;

import br.com.feedhub.application.usecases.feedback.CreateFeedback;
import br.com.feedhub.application.usecases.feedback.ListUserFeedback;
import br.com.feedhub.interfaces.dto.request.feedback.FeedbackCreateRequest;
import br.com.feedhub.interfaces.dto.request.feedback.FeedbackUpdateRequest;
import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Feedback Endpoint")
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final CreateFeedback createFeedback;
    private final ListUserFeedback listUserFeedback;

    public FeedbackController(CreateFeedback createFeedback, ListUserFeedback listUserFeedback) {
        this.createFeedback = createFeedback;
        this.listUserFeedback = listUserFeedback;
    }

    @Operation(
            summary = "Add new Feedback",
            description = "Adds a new Feedback by passing in a JSON",
            security = { @SecurityRequirement(name = "bearerAuth") },
            responses = {
                    @ApiResponse(
                            description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = FeedbackResponse.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody FeedbackCreateRequest feedback, HttpServletRequest request) {
        FeedbackResponse feedbackResponse = createFeedback.execute(feedback, request);
        return ResponseEntity.ok().body(feedbackResponse);
    }

    @Operation(
            summary = "Update a Feedback",
            description = "Update a Feedback by passing in a JSON",
            security = { @SecurityRequirement(name = "bearerAuth") },
            responses = {
                    @ApiResponse(
                            description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = FeedbackResponse.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody FeedbackUpdateRequest feedback, HttpServletRequest request) {
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "List Feedbacks",
            description = "List created Feedbacks",
            security = { @SecurityRequirement(name = "bearerAuth") },
            responses = {
                    @ApiResponse(
                            description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PageListResponse.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @GetMapping("/list")
    public ResponseEntity<?> list(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "title") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String sortDirection,
            HttpServletRequest request
    ) {

        var listFeedback = listUserFeedback.execute(
                title,
                description,
                category,
                status,
                page,
                size,
                sortBy,
                sortDirection,
                request
        );
        return ResponseEntity.ok().body(listFeedback);
    }

}
