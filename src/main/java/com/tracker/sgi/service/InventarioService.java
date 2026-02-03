package com.tracker.sgi.service;

import com.tracker.sgi.dto.response.MovimientoResponseDto;
import com.tracker.sgi.exception.InvalidStockMovementException;
import com.tracker.sgi.repository.ProductoRepository;
import com.tracker.sgi.util.enums.TipoMovimientoEnum;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tracker.sgi.entities.MovimientoInventario;
import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.exception.ResourceNotFoundException;
import com.tracker.sgi.repository.MovimientoInventarioRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventarioService {    
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public MovimientoInventario registrarMovimiento(Productos producto, TipoMovimientoEnum tipoMovimiento,
            int cantidad) {

        Integer stockActual = obtenerStockActual(producto.getId());

        if (tipoMovimiento == TipoMovimientoEnum.SALIDA || tipoMovimiento == TipoMovimientoEnum.AJUSTE_NEGATIVO) {
            if (stockActual < cantidad) {
                throw new InvalidStockMovementException("Stock insuficiente para el producto: " + producto.getNombre()
                        + ". Stock actual: " + stockActual + ", solicitado: " + cantidad);
            }
        }

        int stockResultante = (tipoMovimiento == TipoMovimientoEnum.SALIDA
                || tipoMovimiento == TipoMovimientoEnum.AJUSTE_NEGATIVO)
                        ? stockActual - cantidad
                        : stockActual + cantidad;

        MovimientoInventario movimiento = MovimientoInventario.builder()
                .producto(producto)
                .tipo_movimiento(tipoMovimiento)
                .cantidad(cantidad)
                .stock_resultante(stockResultante)
                .fecha_movimiento(LocalDateTime.now())
                .build();

        movimientoInventarioRepository.save(movimiento);
        producto.setStock_actual(obtenerStockActual(producto.getId()));
        productoRepository.save(producto);

        return movimiento;
    }

    public Page<MovimientoResponseDto> obtenerTodosLosMovimientos(Pageable pageable) {
        Page<MovimientoInventario> movimiento = movimientoInventarioRepository.findAll(pageable);

        return movimiento.map(res -> new MovimientoResponseDto(
                res.getProducto().getNombre(),
                res.getTipo_movimiento(),
                res.getCantidad(),
                res.getOrden().getId(),
                res.getFecha_movimiento(),
                res.getStock_resultante()));
    }

    public Page<MovimientoResponseDto> obtenerMovimientosPorProducto(Long productoId, Pageable pageable) {
        Page<MovimientoInventario> movimientos = movimientoInventarioRepository.findByProductoId(productoId, pageable);

        if (movimientos.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron movimientos para el producto con ID: " + productoId);
        }

        return movimientos.map(res -> new MovimientoResponseDto(
                res.getProducto().getNombre(),
                res.getTipo_movimiento(),
                res.getCantidad(),
                res.getOrden().getId(),
                res.getFecha_movimiento(),
                res.getStock_resultante()));
    }

    public Integer obtenerStockActual(Long productoId) {
        return productoRepository.calcularStockActual(productoId);
    }
}
