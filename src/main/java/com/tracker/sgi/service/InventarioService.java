package com.tracker.sgi.service;


import com.tracker.sgi.dto.response.MovimientoResponseDto;
import com.tracker.sgi.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tracker.sgi.dto.request.InventarioRequestDto;
import com.tracker.sgi.entities.MovimientoInventario;
import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.exception.ResourceNotFoundException;
import com.tracker.sgi.repository.MovimientoInventarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventarioService {
    private final MovimientosService movimientosService;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final ProductoRepository productoRepository;


    public Page<MovimientoResponseDto> obtenerTodosLosMovimientos(Pageable pageable){
        Page<MovimientoInventario> movimiento = movimientoInventarioRepository.findAll(pageable);

        return movimiento.map(res -> new MovimientoResponseDto(
                res.getProducto().getNombre(),
                res.getTipo_movimiento(),
                res.getCantidad(),
                res.getMotivo(),
                res.getOrden().getId(),
                res.getFecha_movimiento(),
                res.getStock_resultante()
        ));
    }

    public Page<MovimientoResponseDto> obtenerMovimientosPorProducto(Long productoId, Pageable pageable){
        Page<MovimientoInventario> movimientos = movimientoInventarioRepository.findByProductoId(productoId, pageable);

        if (movimientos.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron movimientos para el producto con ID: " + productoId);
        }

        return movimientos.map(res -> new MovimientoResponseDto(
                res.getProducto().getNombre(),
                res.getTipo_movimiento(),
                res.getCantidad(),
                res.getMotivo(),
                res.getOrden().getId(),
                res.getFecha_movimiento(),
                res.getStock_resultante()
        ));
    }

    public void ajustePositivoStock(InventarioRequestDto dto){
        Productos producto = productoRepository.findById(dto.productoId())
                                     .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        movimientosService.ajustePositivo(producto, dto.cantidad(), dto.motivo());

    }

    public void ajusteNegativoStock(InventarioRequestDto dto){
        Productos producto = productoRepository.findById(dto.productoId())
                                     .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        movimientosService.ajusteNegativo(producto, dto.cantidad(), dto.motivo());
    }
}
