package br.com.feedhub.interfaces.controllers;

import br.com.feedhub.application.usecases.security.user.CreateUser;
import br.com.feedhub.application.usecases.security.user.FindUser;
import br.com.feedhub.application.usecases.security.user.ListUser;
import br.com.feedhub.application.usecases.security.user.UpdateUser;
import br.com.feedhub.interfaces.dto.request.UserCreateRequest;
import br.com.feedhub.interfaces.dto.request.UserUpdateRequest;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import br.com.feedhub.interfaces.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Endpoint")
@RestController
@RequestMapping("/user")
public class UserController {

    private final CreateUser createUser;
    private final UpdateUser updateUser;
    private final FindUser findUser;
    private final ListUser listUser;

    public UserController(
            CreateUser createUser,
            UpdateUser updateUser,
            FindUser findUse,
            ListUser listUser
    ) {
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.findUser = findUse;
        this.listUser = listUser;
    }

    @Operation(
            summary = "Add new User",
            description = "Adds a new User by passing in a JSON",
            responses = {
                    @ApiResponse(
                            description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UserCreateRequest user) {
        var userCreated = createUser.create(user);
        return ResponseEntity.ok().body(userCreated);
    }

    @Operation(
            summary = "Update a User",
            description = "Update a new User by passing in a JSON",
            responses = {
                    @ApiResponse(
                            description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserUpdateRequest user) {
        var userUpdated = updateUser.update(user);
        return ResponseEntity.ok().body(userUpdated);
    }

    @Operation(
            summary = "Finds a User",
            description = "Finds a User by passing id",
            responses = {
                    @ApiResponse(
                            description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(
                                    implementation = UserResponse.class
                            ))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @GetMapping(value = "/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        var foundUser = findUser.find(id);
        return ResponseEntity.ok().body(foundUser);
    }

    @Operation(
            summary = "Finds all User",
            description = "Finds all User",
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
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String username,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String sortDirection
            ) {
        var userList = listUser.list(name, username, page, size, sortBy, sortDirection);
        return ResponseEntity.ok().body(userList);
    }


}
