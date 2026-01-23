package com.tracker.sgi.controller;

import com.tracker.sgi.dto.response.ProveedorResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.tracker.sgi.dto.request.ProveedorRequestDto;
import com.tracker.sgi.entities.Proveedores;
import com.tracker.sgi.service.ProveedorServices;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorServices proveedorServices;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> agregarProveedor(@RequestBody ProveedorRequestDto dto) {
        proveedorServices.agregarProveedor(dto);
        return Map.of("message", "Proveedor agregado correctamente");
    }

    @GetMapping
    public Page<ProveedorResponseDto> listarProveedores(Pageable pageable) {
        return proveedorServices.listarProveedores(pageable);
    }

    @GetMapping("/{id}")
    public ProveedorResponseDto obtenerProveedor(@PathVariable Long id) {
        return proveedorServices.obtenerProveedor(id);
    }
    
    @PutMapping("/{id}")
    public Proveedores actualizarProveedor(@PathVariable Long id, @RequestBody ProveedorRequestDto dto) {
        return proveedorServices.actualizarProveedor(id, dto);
    }

    @PatchMapping("/{id}")
    public Proveedores actualizarProveedorParcial(@PathVariable Long id, @RequestBody ProveedorRequestDto dto) {
        return proveedorServices.actualizarProveedorParcial(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminarProveedor(@PathVariable Long id) {
        proveedorServices.eliminarProveedor(id);
    }
}
