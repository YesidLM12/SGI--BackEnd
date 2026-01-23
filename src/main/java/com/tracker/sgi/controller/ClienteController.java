package com.tracker.sgi.controller;

import java.nio.file.AccessDeniedException;
import java.util.Map;

import com.tracker.sgi.dto.response.ClienteResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.sgi.dto.request.ClienteRequestDto;
import com.tracker.sgi.entities.Clientes;
import com.tracker.sgi.service.ClienteService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> crearCliente(@RequestBody ClienteRequestDto cliente) {
        clienteService.crearCliente(cliente);
        return Map.of("message", "Cliente creado exitosamente");
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<ClienteResponseDto>> obtenerClientes(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
         return ResponseEntity.ok(clienteService.obtenerTodosLosClientes(pageable));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClienteResponseDto obtenerClientePorId(@PathVariable Long id) {
        return clienteService.obtenerClientePorId(id);
    }

    @GetMapping("/exists/{email}")
    @ResponseStatus(HttpStatus.OK)
    public boolean existeClientePorEmail(@PathVariable String email) {
        return clienteService.existeClientePorEmail(email);
    }

    @GetMapping("/exists/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean existeClientePorId(@PathVariable Long id) {
        return clienteService.existeClientePorId(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> actualizarCliente(@PathVariable Long id, @RequestBody ClienteRequestDto cliente) {
        clienteService.actualizarCliente(id, cliente);
        return Map.of("message", "Cliente actualizado exitosamente");
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> actualizacionParcial(@PathVariable Long id, @RequestBody ClienteRequestDto dto) {
        clienteService.actualizarCliente(id, dto);
        return Map.of("message", "Cliente actualizado exitosamente");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> eliminarCliente(@PathVariable Long id){
        clienteService.eliminarCliente(id);
        return Map.of("message", "Cliente eliminado exitosamente");
    }

}
