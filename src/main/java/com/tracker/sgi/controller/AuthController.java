package com.tracker.sgi.controller;

import org.springframework.web.bind.annotation.RestController;

import com.tracker.sgi.dto.request.LoginRequestDto;
import com.tracker.sgi.dto.request.RegistroRequestDto;
import com.tracker.sgi.dto.response.LoginResponseDto;
import com.tracker.sgi.service.AuthService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        String token = authService.login(dto);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/registro")
    public ResponseEntity<Map<String, String>> registro(@RequestBody RegistroRequestDto dto) {
        authService.registrarUsuario(dto);
        return ResponseEntity.ok(Map.of("message", "Registro exitoso"));
    }
}
