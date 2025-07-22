package com.socialplatformapi.service;

import com.socialplatformapi.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {

    private final Map<String, User> sessions = new ConcurrentHashMap<>();

    public String createSession(User user) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, user);
        return token;
    }

    public Optional<User> getUserByToken(String token) {
        if (token == null) return Optional.empty();
        return Optional.ofNullable(sessions.get(token));
    }

    public void invalidate(String token) {
        sessions.remove(token);
    }
}
