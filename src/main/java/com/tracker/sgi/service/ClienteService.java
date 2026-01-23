package com.tracker.sgi.service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

import com.tracker.sgi.dto.response.ClienteResponseDto;
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

    public void crearCliente(ClienteRequestDto dto) {

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
        clienteRepository.save(cliente);
    }

    public ClienteResponseDto obtenerClientePorId(Long id) {
        Clientes cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        return new ClienteResponseDto(
                cliente.getNombre() + " " + cliente.getApellido(),
                cliente.getEmail(),
                cliente.getTelefono()
        );
    }

    public Page<ClienteResponseDto> obtenerTodosLosClientes(Pageable pageable) {
        Page<Clientes> clientes = clienteRepository.findAll(pageable);

        return clientes.map(res -> new ClienteResponseDto(
                res.getNombre() + " " + res.getApellido(),
                res.getEmail(),
                res.getTelefono()
        ));

    }

    public void eliminarCliente(Long id){
        Clientes cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        clienteRepository.deleteById(id);
    }

    public void actualizarCliente(Long id, ClienteRequestDto dto) {
        Clientes cliente = clienteRepository.findById(id)
                                   .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        if (clienteRepository.findByEmail(dto.email()).isPresent() &&
                !cliente.getEmail().equals(dto.email())) {
            throw new ResourceNotFoundException("El cliente con el email " + dto.email() + " ya está registrado");
        }

        cliente.setDNI(dto.dni());
        cliente.setNombre(dto.nombre());
        cliente.setApellido(dto.apellido());
        cliente.setTelefono(dto.telefono());
        cliente.setEmail(dto.email());

        clienteRepository.save(cliente);
    }

    public boolean existeClientePorEmail(String email) {
        return clienteRepository.findByEmail(email).isPresent();
    }

    public boolean existeClientePorId(Long id) {
        return clienteRepository.existsById(id);
    }

}
