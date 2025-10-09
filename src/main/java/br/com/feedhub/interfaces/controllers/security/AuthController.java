package br.com.feedhub.interfaces.controllers.security;

import br.com.feedhub.application.usecases.security.auth.Authenticate;
import br.com.feedhub.application.usecases.security.auth.RefreshToken;
import br.com.feedhub.interfaces.dto.request.security.AccountCredentials;
import br.com.feedhub.interfaces.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authenticate Endpoint")
@RestController
@RequestMapping
public class AuthController {

    Authenticate authenticate;
    RefreshToken refresh;

    public AuthController(Authenticate authenticate, RefreshToken refresh) {
        this.authenticate = authenticate;
        this.refresh = refresh;
    }

    @Operation(
            summary = "Authenticate",
            description = "Authenticate a user and returns a token",
            responses = {
                    @ApiResponse(
                            description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TokenResponse.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody AccountCredentials credentials) {
        TokenResponse token = authenticate.signin(credentials);
        return ResponseEntity.ok(token);
    }

    @Operation(
            summary = "Refresh Authenticate",
            description = "Authenticate a user and returns a new token",
            responses = {
                    @ApiResponse(
                            description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TokenResponse.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @PutMapping(value = "/refresh/{username}")
    public ResponseEntity<?> refresh(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        TokenResponse token = refresh.execute(username, refreshToken);
        return ResponseEntity.ok(token);
    }

}
