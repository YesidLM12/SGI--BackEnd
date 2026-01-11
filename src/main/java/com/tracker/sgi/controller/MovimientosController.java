package com.tracker.sgi.controller;

import org.springframework.web.bind.annotation.RestController;

import com.tracker.sgi.entities.MovimientoInventario;
import com.tracker.sgi.service.MovimientosService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
