package com.tracker.sgi.util;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tracker.sgi.entities.Categorias;
import com.tracker.sgi.entities.Rol;
import com.tracker.sgi.entities.Usuarios;
import com.tracker.sgi.repository.CategoriaRepository;
import com.tracker.sgi.repository.RolRepository;
import com.tracker.sgi.repository.UsuarioRepository;
import com.tracker.sgi.util.enums.RolEnum;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataInitializer implements CommandLineRunner {
    private final RolRepository rolRepository;
    private final UsuarioRepository userRepository;
    private final CategoriaRepository categoriaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (rolRepository.count() == 0) {
            rolRepository.saveAll(List.of(
                    new Rol(null, RolEnum.ADMIN),
                    new Rol(null, RolEnum.ALMACENISTA),
                    new Rol(null, RolEnum.VENDEDOR))
                );
        }

        if (categoriaRepository.count() == 0) {
            categoriaRepository.saveAll(List.of(
                    new Categorias(null, "Electrónica"),
                    new Categorias(null, "Papeleria"),
                    new Categorias(null, "Aseo"),
                    new Categorias(null, "Alimentos"))
                );
        }

        if (!userRepository.existsByNombre("admin")) {
            Usuarios admin = Usuarios.builder()
                    .nombre("admin")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("admin123"))
                    .rol(rolRepository.findByRol(RolEnum.ADMIN))
                    .activo(true)
                    .build();
            userRepository.save(admin);
        }
    }
}
