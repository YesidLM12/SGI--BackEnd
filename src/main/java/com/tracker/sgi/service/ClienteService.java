package com.tracker.sgi.service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.tracker.sgi.dto.request.ClienteRequestDto;
import com.tracker.sgi.entities.Clientes;
import com.tracker.sgi.exception.InvalidDataException;
import com.tracker.sgi.exception.ResourceNotFoundException;
import com.tracker.sgi.repository.ClientesRepository;
import com.tracker.sgi.util.validations.ClienteValidate;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClientesRepository clienteRepository;

    public Clientes crearCliente(ClienteRequestDto dto) {

        if (clienteRepository.findByDNI(dto.dni()).isPresent()) {
            throw new InvalidDataException("El cliente con el DNI " + dto.dni() + " ya está registrado");
        }

        Clientes cliente = Clientes.builder()
                .DNI(dto.dni())
                .nombre(dto.nombre())
                .apellido(dto.apellido())
                .telefono(dto.telefono())
                .email(dto.email())
                .fecha_creacion(LocalDateTime.now())
                .build();

        ClienteValidate.validate(cliente);
        return clienteRepository.save(cliente);
    }

    public Clientes obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
    }

    public Page<Clientes> obtenerTodosLosClientes(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    public void eliminarCliente(Long id) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AccessDeniedException("Acción no disponible para el usuario");
        }

        clienteRepository.deleteById(id);
    }

    public Clientes actualizarCliente(Long id, ClienteRequestDto dto) {
        Clientes cliente = obtenerClientePorId(id);

        if (clienteRepository.findByEmail(dto.email()).isPresent() &&
                !cliente.getEmail().equals(dto.email())) {
            throw new ResourceNotFoundException("El cliente con el email " + dto.email() + " ya está registrado");
        }

        cliente.setDNI(dto.dni());
        cliente.setNombre(dto.nombre());
        cliente.setApellido(dto.apellido());
        cliente.setTelefono(dto.telefono());
        cliente.setEmail(dto.email());

        return clienteRepository.save(cliente);
    }

    public boolean existeClientePorEmail(String email) {
        return clienteRepository.findByEmail(email).isPresent();
    }

    public boolean existeClientePorId(Long id) {
        return clienteRepository.existsById(id);
    }

}
