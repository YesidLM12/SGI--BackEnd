package com.tracker.sgi.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.sgi.dto.response.BajoStockResponseDto;
import com.tracker.sgi.dto.response.MovimientoResponseDto;
import com.tracker.sgi.dto.response.ProductoMasVendidoDto;
import com.tracker.sgi.service.ReporteService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReporteController {
    private final ReporteService reporteService;

    @GetMapping("/productos-mas-vendidos")
    public List<ProductoMasVendidoDto> getProductoMasVendido() {
        return reporteService.getProductoMasVendido();
    }

    @GetMapping("/productos-mas-vendidos-por-rango-fecha")
    public List<ProductoMasVendidoDto> getProductoMasVendidoPorRangoFecha(@RequestParam LocalDateTime fechaInicio,
            @RequestParam LocalDateTime fechaFin) {
        return reporteService.getProductoMasVendidoPorRangoFecha(fechaInicio, fechaFin);
    }

    @GetMapping("/productos-mas-vendidos-por-categoria")
    public List<ProductoMasVendidoDto> getProductoMasVendidoPorCategoria(@RequestParam Long categoriaId) {
        return reporteService.getProductoMasVendidoPorCategoria(categoriaId);
    }

    @GetMapping("/productos-con-bajo-stock")
    public Page<BajoStockResponseDto> getProductosConBajoStock(Pageable pageable) {
        return reporteService.getProductosConBajoStock(pageable);
    }

    @GetMapping("/movimientos-por-rango-fecha")
    public Page<MovimientoResponseDto> getMovimientosByDateRange(@RequestParam LocalDateTime fechaInicio,
            @RequestParam LocalDateTime fechaFin, Pageable pageable) {
        return reporteService.getMovimientosByDateRange(fechaInicio, fechaFin, pageable);
    }

    @GetMapping("/exportar")
    public void exportarReporte(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            HttpServletResponse response) throws IOException {
        reporteService.exportarReporte(fechaInicio, fechaFin, response);
    }
}
