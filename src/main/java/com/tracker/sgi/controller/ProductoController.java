package com.tracker.sgi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.sgi.dto.request.ActualizarProductoRequestDto;
import com.tracker.sgi.dto.request.ProductoRequestDto;
import com.tracker.sgi.dto.response.ProductoResponseDto;
import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.service.ProductoService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/productos")
@PreAuthorize("hasAnyRole('ADMIN','ALMACENISTA')")
public class ProductoController {
    private final ProductoService productoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> crearProducto(@RequestBody ProductoRequestDto dto) {
        System.out.println("ENTRÉ AL MÉTODO");

        productoService.agregarProducto(dto);
        return Map.of("message", "Producto creado exitosamente");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> obtenerProducto(@PathVariable Long id) {
        Productos producto = productoService.obtenerProductoPorId(id);

        return ResponseEntity.ok(new ProductoResponseDto(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock_actual(),
                producto.getStock_minimo(),
                producto.getCategoria().getNombre(),
                producto.getFecha_creacion()));
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDto>> listarProductos() {
        List<ProductoResponseDto> productos = productoService.obtenerTodosLosProductos(Pageable.unpaged()).stream()
                .map(producto -> new ProductoResponseDto(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getPrecio(),
                        producto.getStock_actual(),
                        producto.getStock_minimo(),
                        producto.getCategoria().getNombre(),
                        producto.getFecha_creacion()))
                .toList();
        return ResponseEntity.ok(productos);
    }

    @PutMapping("/{id}")
    public Map<String, String> actualizarProducto(@PathVariable Long id, @RequestBody ProductoRequestDto dto)
            throws AccessDeniedException {
        productoService.actualizarProductoCompleto(id, dto);
        return Map.of("message", "Producto actualizado exitosamente");
    }

    @PatchMapping("/{id}")
    public Map<String, String> actualizarProductoParcial(@PathVariable Long id, @RequestBody ActualizarProductoRequestDto dto) {
        productoService.actualizarProductoParcial(id, dto);
        return Map.of("message", "Producto actualizado exitosamente");
    }   

    @DeleteMapping("/{id}")
    public Map<String, String> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProductoPorId(id);
        return Map.of("message", "Producto eliminado exitosamente");
    }
}
