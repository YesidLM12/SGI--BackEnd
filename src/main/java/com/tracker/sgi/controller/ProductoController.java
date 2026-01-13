package com.tracker.sgi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.sgi.dto.request.ProductoRequestDto;
import com.tracker.sgi.service.ProductoService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/productos")
@PreAuthorize("hasAnyRole('ADMIN','ALMACENISTA')")
public class ProductoController {
    private final ProductoService productoService;

    @PutMapping("/{id}")
    public void actualizarProducto(@PathVariable Long id, @RequestBody ProductoRequestDto dto)
            throws AccessDeniedException {
        productoService.actualizarProductoCompleto(id, dto);
    }

    @PatchMapping("/{id}/precio")
    public void actualizarPrecio(@PathVariable Long id, @RequestBody BigDecimal precio) {
        productoService.actualizarPrecio(id, precio);
    }

    @PatchMapping("/{id}/stock-minimo")
    public void actualizarStockMinimo(@PathVariable Long id, @RequestBody int stockMinimo) {
        productoService.actualizarStockMinimo(id, stockMinimo);
    }

    @PatchMapping("/{id}/disponibilidad")
    public void actualizarDisponibilidad(@PathVariable Long id, @RequestBody boolean disponible) {
        productoService.actualizarDisponibilidad(id, disponible);
    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProductoPorId(id);
    }

    @DeleteMapping("/{nombre}")
    public void eliminarProductoPorNombre(@PathVariable String nombre) {
        productoService.eliminarProductoPorNombre(nombre);
    }
}
