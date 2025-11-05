package br.com.feedhub.interfaces.controllers.dashboard;

import br.com.feedhub.application.usecases.dashboard.DashboardStatistics;
import br.com.feedhub.interfaces.dto.response.CommentResponse;
import br.com.feedhub.interfaces.dto.response.DashboardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Dashboard View Stats")
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardStatistics dashboardStatistics;

    public DashboardController(DashboardStatistics dashboardStatistics) {
        this.dashboardStatistics = dashboardStatistics;
    }

    @Operation(
            summary = "Dashboard Stats",
            description = "Return dashboard statistics",
            security = { @SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(
                            description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = DashboardResponse.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)

            }
    )
    @GetMapping("/statistics")
    public ResponseEntity<?> stats(@RequestParam Integer startMonth, @RequestParam Integer endMonth, HttpServletRequest request) {
        var statistics = dashboardStatistics.execute(startMonth, endMonth, request);
        return ResponseEntity.ok().body(statistics);
    }



}
