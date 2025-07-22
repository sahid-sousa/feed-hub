package br.com.feedhub.interfaces.controllers;

import br.com.feedhub.application.usecases.security.auth.Authenticate;
import br.com.feedhub.interfaces.dto.request.AccountCredentials;
import br.com.feedhub.interfaces.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authenticate Endpoint")
@RestController
@RequestMapping
public class AuthController {

    Authenticate authenticate;

    public AuthController(Authenticate authenticate) {
        this.authenticate = authenticate;
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

}
