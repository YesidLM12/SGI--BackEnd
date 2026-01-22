package com.tracker.sgi.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tracker.sgi.dto.response.DetallesResponseDto;
import com.tracker.sgi.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tracker.sgi.repository.DetallesOrdenRepository;
import com.tracker.sgi.repository.MovimientoInventarioRepository;
import com.tracker.sgi.repository.OrdenRepository;
import com.tracker.sgi.repository.ProductoRepository;
import com.tracker.sgi.repository.ProveedoresRepository;
import com.tracker.sgi.repository.UsuarioRepository;
import com.tracker.sgi.util.enums.EstadoOrdenEnum;
import com.tracker.sgi.util.enums.TipoOrdenEnum;
import com.tracker.sgi.dto.request.DetallesRequestDto;
import com.tracker.sgi.dto.request.OrdenRequestDto;
import com.tracker.sgi.dto.response.OrdenResponseDto;
import com.tracker.sgi.exception.ResourceNotFoundException;
import com.tracker.sgi.repository.ClientesRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrdenService {

	private final OrdenRepository ordenesRepository;
	private final MovimientoInventarioRepository movimientoInventarioRepository;
	private final ProveedoresRepository proveedoresRepository;
	private final UsuarioRepository usuarioRepository;
	private final DetallesOrdenRepository detallesOrdenRepository;
	private final ProductoRepository productosRepository;
	private final ClientesRepository clientesRepository;
	private final MovimientosService movimientoInventarioService;

	@Transactional
	public void crearOrden(OrdenRequestDto dto) {
		Proveedores proveedor = proveedoresRepository.findById(dto.proveedorId())
						                        .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

		Clientes cliente = clientesRepository.findById(dto.clienteId())
						                   .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

		Usuarios usuario = usuarioRepository.findById(dto.usuarioId())
						                   .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		Ordenes orden = Ordenes.builder()
						                .estado(EstadoOrdenEnum.PENDIENTE)
						                .fecha(LocalDateTime.now())
						                .proveedor(proveedor)
						                .cliente(cliente)
						                .usuario(usuario)
						                .tipo(dto.tipo())
						                .build();

		ordenesRepository.save(orden);

		List<DetallesOrden> detalles = new ArrayList<>();

		for (DetallesRequestDto detalle : dto.detalles()) {
			Productos producto = productosRepository.findById(detalle.productoId())
							                     .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

			DetallesOrden detalleOrden = DetallesOrden.builder()
							                             .orden(orden)
							                             .producto(producto)
							                             .cantidad(detalle.cantidad())
							                             .precio_unitario(orden.getTipo() == TipoOrdenEnum.COMPRA
											                                              ? detalle.precioUnitario()
											                                              : producto.getPrecio())
							                             .build();

			detallesOrdenRepository.save(detalleOrden);
			detalles.add(detalleOrden);
		}

		orden.setDetalles(detalles);
		orden.setTotal(orden.calcularTotal());
		ordenesRepository.save(orden);
	}

	@Transactional
	public void confirmarOrden (Long ordenId){
		Ordenes orden = ordenesRepository.findById(ordenId)
						                .orElseThrow(() -> new RuntimeException("Orden no encontrado"));

		orden.setEstado(EstadoOrdenEnum.EN_PROCESO);

		List<DetallesOrden> detalles = orden.getDetalles();

		if (orden.getTipo() == TipoOrdenEnum.COMPRA) {
			for (DetallesOrden detalle : detalles) {
				Productos producto = detalle.getProducto();
				int cantidad = detalle.getCantidad();

				MovimientoInventario movimiento = movimientoInventarioService.entrada(producto,cantidad,"Compra");
				movimientoInventarioRepository.save(movimiento);
			}
		}

		if (orden.getTipo() == TipoOrdenEnum.VENTA){
			for (DetallesOrden detalle : detalles) {
				Productos producto = detalle.getProducto();
				int cantidad = detalle.getCantidad();

				MovimientoInventario movimiento = movimientoInventarioService.salida(producto,cantidad,"Venta");
				movimientoInventarioRepository.save(movimiento);
			}

			orden.setEstado(EstadoOrdenEnum.COMPLETADO);
			ordenesRepository.save(orden);
		}
	}


	public void cancelarOrden (Long ordenId) {
		Ordenes orden = ordenesRepository.findById(ordenId)
						                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrado"));

		orden.cancelar();
		ordenesRepository.save(orden);
	}

	public Page<OrdenResponseDto> obtenerOrdenes (Pageable pageable) {
		Page<Ordenes>  ordenes = ordenesRepository.findAll(pageable);

		List<DetallesOrden> detalles = ordenes.getContent().getFirst().getDetalles();
		List<DetallesResponseDto> detallesResponse = new ArrayList<>();

		for (DetallesOrden detalle : detalles) {
			DetallesResponseDto response = new DetallesResponseDto(
							detalle.getProducto().getNombre(),
							detalle.getCantidad(),
							detalle.getPrecio_unitario()
			);

			detallesResponse.add(response);
		}
		return ordenes.map(res -> new OrdenResponseDto(
						res.getEstado(),
						res.getProveedor() != null ? res.getProveedor().getId() : null,
						res.getCliente() != null ? res.getCliente().getId() : null,
						res.getUsuario() != null ? res.getUsuario().getId() : null,
						res.getTipo(),
						res.getTotal(),
						detallesResponse));
	}

	public OrdenResponseDto obtenerOrdenPorId(Long ordenId) {
		Ordenes orden = ordenesRepository.findById(ordenId)
						                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrado"));

		List<DetallesOrden> detalles = orden.getDetalles();
		List<DetallesResponseDto> detallesResponse = new ArrayList<>();

		for(DetallesOrden d :  detalles) {
			DetallesResponseDto response = new DetallesResponseDto(
							d.getProducto().getNombre(),
							d.getCantidad(),
							d.getPrecio_unitario()
			);

			detallesResponse.add(response);
		}

		return new OrdenResponseDto(
						orden.getEstado(),
						orden.getProveedor() != null ? orden.getProveedor().getId() : null,
						orden.getCliente() != null ? orden.getCliente().getId() : null,
						orden.getUsuario() != null ? orden.getUsuario().getId() : null,
						orden.getTipo(),
						orden.getTotal(),
						detallesResponse
		);
	}
}
