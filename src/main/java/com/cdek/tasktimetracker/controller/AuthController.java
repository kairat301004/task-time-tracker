package com.cdek.tasktimetracker.controller;

import com.cdek.tasktimetracker.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token", description = "Returns JWT token for authenticated requests")
    public Map<String, String> login(@RequestBody Map<String, String> credentials) {
        // Извлекаем логин и пароль из тела запроса
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Временная проверка (заглушка)
        if ("admin".equals(username) && "admin123".equals(password)) {
            String token = jwtUtil.generateToken(username);
            return Map.of("token", token, "username", username);
        }

        throw new RuntimeException("Неверный логин или пароль");
    }
}
