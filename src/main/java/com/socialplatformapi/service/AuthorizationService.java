package com.socialplatformapi.service;

import com.socialplatformapi.exception.auth.AuthenticationException;
import com.socialplatformapi.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthorizationService {
    private final SessionService sessionService;

    public User getLoggedInUser(HttpServletRequest request) {
        String token = request.getHeader("X-Session-Token");

        if (token == null) {
            throw new AuthenticationException("Missing session token");
        }

        return sessionService.getUserByToken(token)
                .orElseThrow(() -> new AuthenticationException("Invalid or expired session token"));
    }
}
