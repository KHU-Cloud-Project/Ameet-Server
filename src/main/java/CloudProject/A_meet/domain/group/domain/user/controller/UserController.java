package CloudProject.A_meet.domain.group.domain.user.controller;

import CloudProject.A_meet.domain.group.domain.user.dto.UserLoginRequest;
import CloudProject.A_meet.domain.group.domain.user.dto.UserResponse;
import CloudProject.A_meet.domain.group.domain.user.dto.UserSignupRequest;
import CloudProject.A_meet.domain.group.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Tag(name = "User", description = "User API")
@Validated
public class UserController {
    private final UserService userService;

    @Operation(summary = "User Signup", description = "Use this to register a new user account in the system.")
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserSignupRequest userSignupRequest) {
        UserResponse.UserData userData = userService.registerUser(userSignupRequest);
        UserResponse response = new UserResponse(true, 201, userData);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "User Login", description = "Use this to authenticate and log in an existing user.")
    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        UserResponse.UserData userData = userService.authenticateUser(userLoginRequest);
        UserResponse response = new UserResponse(true, 200, userData);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get User Info", description = "Fetch the current logged-in user's information.")
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        UserResponse.UserData userData = userService.getUserById(userId);
        UserResponse response = new UserResponse(true, 200, userData);
        return ResponseEntity.ok(response);
    }

}
