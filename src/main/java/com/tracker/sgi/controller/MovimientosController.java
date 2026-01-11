package com.tracker.sgi.controller;

import org.springframework.web.bind.annotation.RestController;

import com.tracker.sgi.dto.request.InventarioRequestDto;
import com.tracker.sgi.entities.MovimientoInventario;
import com.tracker.sgi.service.MovimientosService;

import lombok.RequiredArgsConstructor;

import java.nio.file.AccessDeniedException;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movimientos")
public class MovimientosController {
    private final MovimientosService movimientosService;

    @GetMapping()
    public Page<MovimientoInventario> obtenerTodosLosMovimientos(
            @PageableDefault(size = 10, sort = "nombre") Pageable pageable) {
        return movimientosService.obtenerTodosLosMovimientos(pageable);
    }

    @GetMapping("/{productoId}")
    public Page<MovimientoInventario> obtenerMovimientosPorProducto(@PathVariable Long productoId,
            @PageableDefault(size = 10, sort = "nombre") Pageable pageable) {
        return movimientosService.obtenerMovimientosPorProducto(productoId, pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ALMACENISTA')")
    @PostMapping("/ajuste-positivo")
    public Map<String, String> ajustePositivo(@RequestBody InventarioRequestDto dto) throws AccessDeniedException {
        movimientosService.ajustePositivo(dto.producto(), dto.cantidad(), dto.motivo());
        return Map.of("message", "Ajuste realizado correctamente.");
    }

    @PreAuthorize("hasAnyRole('ADMIN','ALMACENISTA')")
    @PostMapping("/ajuste-negativo")
    public Map<String, String> ajusteNegativo(@RequestBody InventarioRequestDto dto) throws AccessDeniedException {
        movimientosService.ajusteNegativo(dto.producto(), dto.cantidad(), dto.motivo());
        return Map.of("message", "Ajuste realizado correctamente.");
    }
}
