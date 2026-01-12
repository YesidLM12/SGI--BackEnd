package com.tracker.sgi.service;

import java.nio.file.AccessDeniedException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tracker.sgi.dto.request.InventarioRequestDto;
import com.tracker.sgi.entities.MovimientoInventario;
import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.exception.ResourceNotFoundException;
import com.tracker.sgi.repository.MovimientoInventarioRepository;
import com.tracker.sgi.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final ProductoService productosService;
    private final MovimientosService movimientosService;
    private final ProductoRepository productosRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    public void entradaStock(InventarioRequestDto dto) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdminOrAlmacenista = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")
                        || auth.getAuthority().equals("ROLE_ALMACENISTA"));

        if (!isAdminOrAlmacenista) {
            throw new AccessDeniedException("Acción no disponible para el usuario");
        }

        Productos producto = productosService.obtenerProductoPorId(dto.productoId());

        movimientosService.entrada(producto, dto.cantidad(), dto.motivo());

        producto.setStock_actual(productosRepository.calcularStockActual(producto.getId()));

        productosRepository.save(producto);

    }

    public void salidaStock(InventarioRequestDto dto) throws AccessDeniedException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdminOrAlmacenista = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")
                        || auth.getAuthority().equals("ROLE_ALMACENISTA"));

        if (!isAdminOrAlmacenista) {
            throw new AccessDeniedException("Acción no disponible para el usuario");
        }
        Productos producto = productosService.obtenerProductoPorId(dto.productoId());

        movimientosService.salida(producto, dto.cantidad(), dto.motivo());

        producto.setStock_actual(productosRepository.calcularStockActual(producto.getId()));

        productosRepository.save(producto);

    }

    public Page<Productos> obtenerTodosLosProductos(Pageable pageable) {
        return productosService.obtenerTodosLosProductos(pageable);
    }

    public Productos obtenerProductoPorId(Long id) {
        return productosService.obtenerProductoPorId(id);
    }

    public Page<MovimientoInventario> obtenerTodosLosMovimientos(Pageable pageable) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdminOrAlmacenista = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")
                        || auth.getAuthority().equals("ROLE_ALMACENISTA"));

        if (!isAdminOrAlmacenista) {
            throw new AccessDeniedException("Acción no disponible para el usuario");
        }
        return movimientoInventarioRepository.findAll(pageable);
    }

    public Page<MovimientoInventario> obtenerMovimientosPorProducto(Long productoId, Pageable pageable)
            throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdminOrAlmacenista = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")
                        || auth.getAuthority().equals("ROLE_ALMACENISTA"));

        if (!isAdminOrAlmacenista) {
            throw new AccessDeniedException("Acción no disponible para el usuario");
        }
        Page<MovimientoInventario> movimientos = movimientoInventarioRepository.findByProductoId(productoId, pageable);

        if (movimientos.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron movimientos para el producto con ID: " + productoId);
        }

        return movimientos;
    }

    public void ajustePositivoStock(InventarioRequestDto dto) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdminOrAlmacenista = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")
                        || auth.getAuthority().equals("ROLE_ALMACENISTA"));

        if (!isAdminOrAlmacenista) {
            throw new AccessDeniedException("Acción no disponible para el usuario");
        }
        Productos producto = productosService.obtenerProductoPorId(dto.productoId());
        movimientosService.ajustePositivo(producto, dto.cantidad(), dto.motivo());

    }

    public void ajusteNegativoStock(InventarioRequestDto dto) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdminOrAlmacenista = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")
                        || auth.getAuthority().equals("ROLE_ALMACENISTA"));

        if (!isAdminOrAlmacenista) {
            throw new AccessDeniedException("Acción no disponible para el usuario");
        }
        Productos producto = productosService.obtenerProductoPorId(dto.productoId());
        movimientosService.ajusteNegativo(producto, dto.cantidad(), dto.motivo());
    }
}
