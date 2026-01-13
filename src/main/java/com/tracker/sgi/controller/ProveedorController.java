package com.tracker.sgi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.sgi.dto.request.ProveedorRequestDto;
import com.tracker.sgi.entities.Proveedores;
import com.tracker.sgi.service.ProveedorServices;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/proveedores")
public class ProveedorController {

    private final ProveedorServices proveedorServices;

    @PostMapping
    public Proveedores agregarProveedor(@RequestBody ProveedorRequestDto dto) {
        return proveedorServices.agregarProveedor(dto);
    }

    @GetMapping
    public Page<Proveedores> listarProveedores(Pageable pageable) {
        return proveedorServices.listarProveedores(pageable);
    }

    @GetMapping("/{id}")
    public Proveedores obtenerProveedor(@PathVariable Long id) {
        return proveedorServices.obtenerProveedor(id);
    }
    
    @PutMapping("/{id}")
    public Proveedores actualizarProveedor(@PathVariable Long id, @RequestBody ProveedorRequestDto dto) {
        return proveedorServices.actualizarProveedor(id, dto);
    }

    @PatchMapping("/{id}")
    public Proveedores actualizarProveedorParcial(@PathVariable Long id, @RequestBody ProveedorRequestDto dto) {
        return proveedorServices.actualizarProveedor(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminarProveedor(@PathVariable Long id) {
        proveedorServices.eliminarProveedor(id);
    }
}
