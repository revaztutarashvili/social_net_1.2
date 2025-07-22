package com.socialplatformapi.controller;

import com.socialplatformapi.dto.auth.UserLoginRequest;
import com.socialplatformapi.dto.register.UserRegisterRequest;
import com.socialplatformapi.dto.user.UserSummary;
import com.socialplatformapi.service.SessionService;
import com.socialplatformapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final SessionService sessionService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(token);
    }

    @Operation(
            summary = "Logout",
            parameters = {
                    @Parameter(name = "X-Session-Token", in = ParameterIn.HEADER, required = true, description = "Session token")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader("X-Session-Token");
        sessionService.invalidate(token);
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping
    public List<UserSummary> getUsers(@RequestParam(defaultValue = "0") int page) {
        return userService.getAllUserNames(PageRequest.of(page, 10, Sort.Direction.ASC, "firstName"));
    }
}
