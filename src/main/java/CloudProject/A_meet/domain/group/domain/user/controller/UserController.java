package CloudProject.A_meet.domain.group.domain.user.controller;

import CloudProject.A_meet.domain.group.domain.user.dto.UserResponse;
import CloudProject.A_meet.domain.group.domain.user.dto.UserSignupRequest;
import CloudProject.A_meet.domain.group.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Tag(name = "User", description = "User API")
@Validated
public class UserController {
    private final UserService userService;

    @Operation(summary = "get space list", description = "use this when get own space list or search space list")
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserSignupRequest userSignupRequest) {
        UserResponse.UserData userData = userService.registerUser(userSignupRequest);
        UserResponse response = new UserResponse(true, 201, userData);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
