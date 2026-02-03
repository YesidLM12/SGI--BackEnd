package com.tracker.sgi.service;

import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.tracker.sgi.dto.request.LoginRequestDto;
import com.tracker.sgi.dto.request.RegistroRequestDto;
import com.tracker.sgi.entities.Usuarios;
import com.tracker.sgi.repository.RolRepository;
import com.tracker.sgi.repository.UsuarioRepository;
import com.tracker.sgi.security.jwt.JwtProvider;

@Service
@RequiredArgsConstructor
@Profile("!test")
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RolRepository rolRepository;

    public void registrarUsuario(RegistroRequestDto dto) {
        Usuarios usuarioExistente = usuarioRepository.findByEmail(dto.email());

        if (usuarioExistente != null) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuarios usuario = Usuarios.builder()
                .nombre(dto.nombre())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .rol(rolRepository.findByRol(dto.rol()))
                .activo(dto.activo() != null ? dto.activo() : true)
                .build();
        usuarioRepository.save(usuario);
    }

    public String login(LoginRequestDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String token = jwtProvider.generateToken(user);

        return token;
    }
}
