package com.tracker.sgi.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

import com.tracker.sgi.dto.request.AlertaStockDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tracker.sgi.dto.response.MovimientoResponseDto;
import com.tracker.sgi.dto.request.ProductoMasVendidoDto;
import com.tracker.sgi.exception.InvalidDataException;
import com.tracker.sgi.repository.DetallesOrdenRepository;
import com.tracker.sgi.repository.MovimientoInventarioRepository;
import com.tracker.sgi.repository.ProductoRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteService {
    private final DetallesOrdenRepository detallesOrdenRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    public List<ProductoMasVendidoDto> getProductoMasVendido() {
        return detallesOrdenRepository.findProductoMasVendido();
    }

    public List<ProductoMasVendidoDto> getProductoMasVendidoPorRangoFecha(LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {
        return detallesOrdenRepository.findProductoMasVendidoPorRangoFecha(fechaInicio, fechaFin);
    }

    public List<ProductoMasVendidoDto> getProductoMasVendidoPorCategoria(Long categoriaId) {
        return detallesOrdenRepository.findProductoMasVendidoPorCategoria(categoriaId);
    }

    public Page<MovimientoResponseDto> getMovimientosByDateRange(LocalDateTime fechaInicio, LocalDateTime fechaFin,
            Pageable pageable) {
        return movimientoInventarioRepository.findMovimientosByDateRange(fechaInicio, fechaFin, pageable);
    }

    public void exportarReporte(LocalDateTime fechaInicio, LocalDateTime fechaFin, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=reporte.csv");

        if (fechaInicio == null || fechaFin == null) {
            fechaInicio = LocalDateTime.now().minusDays(30);
            fechaFin = LocalDateTime.now();
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new InvalidDataException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        List<ProductoMasVendidoDto> productos = getProductoMasVendido();
        List<MovimientoResponseDto> movimientos = getMovimientosByDateRange(
                fechaInicio,
                fechaFin,
                Pageable.unpaged()).getContent();

        PrintWriter writer = response.getWriter();
        writer.println("REPORTE: Productos más vendidos");
        writer.println("Producto,Cantidad,Ventas");

        for (ProductoMasVendidoDto producto : productos) {
            writer.println(
                    producto.nombre_producto() + "," +
                            producto.cantidad_vendida() + "," +
                            producto.total_recaudado());
        }

        writer.println();

        writer.println("REPORTE: Movimientos de inventario");
        writer.println("Producto,Movimiento,Cantidad,Motivo,Orden ID,Fecha,Stock Resultante");

        for (MovimientoResponseDto movimiento : movimientos) {
            writer.println(
                    movimiento.producto() + "," +
                            movimiento.tipo_movimiento() + "," +
                            movimiento.cantidad() + "," +
                            movimiento.orden_id() + "," +
                            movimiento.fecha_movimiento() + "," +
                            movimiento.stock_resultante());
        }

        writer.flush();
    }
}
