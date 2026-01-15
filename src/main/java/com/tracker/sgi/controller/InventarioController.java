package com.tracker.sgi.controller;

import lombok.RequiredArgsConstructor;

import java.nio.file.AccessDeniedException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.sgi.dto.request.InventarioRequestDto;
import com.tracker.sgi.entities.MovimientoInventario;
import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.service.InventarioService;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {
    private final InventarioService inventarioService;

    @GetMapping("/producto")
    public Page<Productos> obtenerTodosLosProductos(@PageableDefault(size = 10, sort = "nombre") Pageable pageable) {
        return inventarioService.obtenerTodosLosProductos(pageable);
    }

    @GetMapping("/producto/{id}")
    public Productos obtenerProducto(@PathVariable Long id) {
        return inventarioService.obtenerProductoPorId(id);
    }

    @GetMapping("/movimiento")
    public Page<MovimientoInventario> obtenerTodosLosMovimientos(
            @PageableDefault(size = 10, sort = "fecha_movimiento") Pageable pageable) throws AccessDeniedException {
        return inventarioService.obtenerTodosLosMovimientos(pageable);
    }

    @GetMapping("/movimiento/producto/{id}")
    public Page<MovimientoInventario> obtenerMovimientosPorProducto(@PathVariable Long id,
            @PageableDefault(size = 10, sort = "fecha_movimiento") Pageable pageable) throws AccessDeniedException {
        return inventarioService.obtenerMovimientosPorProducto(id, pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENISTA')")
    @PostMapping("/ajuste/positivo")
    public void ajustePositivoStock(@RequestBody InventarioRequestDto dto) throws AccessDeniedException {
        inventarioService.ajustePositivoStock(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENISTA')")
    @PostMapping("/ajuste/negativo")
    public void ajusteNegativoStock(@RequestBody InventarioRequestDto dto) throws AccessDeniedException {
        inventarioService.ajusteNegativoStock(dto);
    }
}
