package com.tracker.sgi.service;


import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.sgi.dto.request.DetallesRequestDto;
import com.tracker.sgi.dto.request.OrdenRequestDto;
import com.tracker.sgi.entities.Clientes;
import com.tracker.sgi.entities.DetallesOrden;
import com.tracker.sgi.entities.MovimientoInventario;
import com.tracker.sgi.entities.Ordenes;
import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.entities.Proveedores;
import com.tracker.sgi.entities.Usuarios;
import com.tracker.sgi.exception.ResourceNotFoundException;
import com.tracker.sgi.repository.DetallesOrdenRepository;
import com.tracker.sgi.repository.MovimientoInventarioRepository;
import com.tracker.sgi.repository.OrdenRepository;
import com.tracker.sgi.repository.ProductoRepository;
import com.tracker.sgi.repository.ProveedoresRepository;
import com.tracker.sgi.repository.UsuarioRepository;
import com.tracker.sgi.util.enums.EstadoOrdenEnum;
import com.tracker.sgi.util.enums.TipoOrdenEnum;
import com.tracker.sgi.repository.ClientesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final ProveedoresRepository proveedoresRepository;
    private final UsuarioRepository usuarioRepository;
    private final DetallesOrdenRepository detallesOrdenRepository;
    private final ProductoRepository productosRepository;
    private final ClientesRepository clientesRepository;
    private final MovimientosService movimientoInventarioService;

    @Transactional
    public Ordenes comprarOrden(OrdenRequestDto dto) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdminOrAlmacenista = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")
                        || auth.getAuthority().equals("ROLE_ALMACENISTA"));

        if (!isAdminOrAlmacenista) {
            throw new AccessDeniedException("Acción no disponible para el usuario");
        }

        Proveedores proveedor = proveedoresRepository.findById(dto.proveedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));

        Usuarios usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Ordenes orden = Ordenes.builder()
                .tipo(dto.tipo())
                .proveedor(proveedor)
                .usuario(usuario)
                .build();

        List<DetallesOrden> detalle = new ArrayList<>();

        for (DetallesRequestDto d : dto.detalles()) {

            Productos producto = productosRepository.findById(d.productoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            DetallesOrden data = DetallesOrden.builder()
                    .orden(orden)
                    .producto(producto)
                    .cantidad(d.cantidad())
                    .precio_unitario(d.precioUnitario())
                    .build();
            detalle.add(data);
            detallesOrdenRepository.save(data);

            producto.setProveedor(proveedor);

            MovimientoInventario movimiento = movimientoInventarioService.entrada(producto, d.cantidad(),
                    "Entrada por orden");
            movimientoInventarioRepository.save(movimiento);

        }

        orden.setDetalles(detalle);
        orden.calcularTotal();
        ordenRepository.save(orden);

        return orden;
    }

    @Transactional
    public Ordenes VentaOrden(OrdenRequestDto dto) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdminOrAlmacenista = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")
                        || auth.getAuthority().equals("ROLE_ALMACENISTA"));

        if (!isAdminOrAlmacenista) {
            throw new AccessDeniedException("Acción no disponible para el usuario");
        }

        Clientes cliente = clientesRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        Usuarios usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Ordenes orden = Ordenes.builder()
                .tipo(dto.tipo())
                .cliente(cliente)
                .usuario(usuario)
                .build();

        List<DetallesOrden> detalle = new ArrayList<>();

        for (DetallesRequestDto d : dto.detalles()) {

            Productos producto = productosRepository.findById(d.productoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            DetallesOrden data = DetallesOrden.builder()
                    .orden(orden)
                    .producto(producto)
                    .cantidad(d.cantidad())
                    .precio_unitario(producto.getPrecio())
                    .build();
            detalle.add(data);
            detallesOrdenRepository.save(data);

            orden.setCliente(cliente);

            MovimientoInventario movimiento = movimientoInventarioService.salida(producto, d.cantidad(),
                    "Salida por orden");
            movimientoInventarioRepository.save(movimiento);
        }

        orden.setDetalles(detalle);
        orden.calcularTotal();
        ordenRepository.save(orden);

        return orden;
    }

    public Page<Ordenes> obtenerTodasLasOrdenes(Pageable pageable) {
        return ordenRepository.findAll(pageable);
    }

    public Page<Ordenes> obtenerOrdenesPorCliente(Long clienteId, Pageable pageable) {
        return ordenRepository.findByClienteId(clienteId, pageable);
    }

    public Page<Ordenes> obtenerOrdenesPorProveedor(Long proveedorId, Pageable pageable) {
        return ordenRepository.findByProveedorId(proveedorId, pageable);
    }

    public Page<Ordenes> obtenerOrdenesPorTipo(TipoOrdenEnum tipo, Pageable pageable) {
        return ordenRepository.findByTipo(tipo, pageable);
    }

    public Page<Ordenes> obtenerOrdenesPorEstado(EstadoOrdenEnum estado, Pageable pageable) {
        return ordenRepository.findByEstado(estado, pageable);
    }

    public Page<Ordenes> obtenerOrdenesPorFecha(LocalDateTime fecha, Pageable pageable) {
        return ordenRepository.findByFecha(fecha, pageable);
    }

}
