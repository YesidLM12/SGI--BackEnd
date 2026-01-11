package com.tracker.sgi.controller;

import org.springframework.web.bind.annotation.RestController;

import com.tracker.sgi.dto.request.ProductoRequestDto;
import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.service.ProductoService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService productoService;

    @PutMapping("/{id}")
    public void actualizarProducto(@PathVariable Long id, @RequestBody ProductoRequestDto dto) {
        productoService.actualizarProductoCompleto(id, dto);
    }

    @PatchMapping("/{id}/precio-compra")
    public void actualizarPrecioCompra(@PathVariable Long id, @RequestBody BigDecimal precioCompra) {
        productoService.actualizarPrecioCompra(id, precioCompra);
    }

    @PatchMapping("/{id}/precio-venta")
    public void actualizarPrecioVenta(@PathVariable Long id, @RequestBody BigDecimal precioVenta) {
        productoService.actualizarPrecioVenta(id, precioVenta);
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

    @GetMapping("/{id}")
    public Productos obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id);
    }

    @GetMapping("/{nombre}")
    public Productos obtenerProductoPorNombre(@PathVariable String nombre) {
        return productoService.obtenerProductoPorNombre(nombre);
    }

    @GetMapping
    public Page<Productos> obtenerTodosLosProductos(@PageableDefault(size = 10, sort = "nombre") Pageable pageable) {
        return productoService.obtenerTodosLosProductos(pageable);
    }
}
