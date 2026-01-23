package com.tracker.sgi.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.core.net.server.Client;
import com.tracker.sgi.dto.response.DetallesResponseDto;
import com.tracker.sgi.entities.*;
import com.tracker.sgi.exception.BusinessRuleException;
import com.tracker.sgi.exception.InvalidDataException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseStatus;

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
		Usuarios usuario = usuarioRepository.findById(dto.usuarioId())
						                   .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

		Ordenes orden = Ordenes.builder()
						                .estado(EstadoOrdenEnum.PENDIENTE)
						                .fecha(LocalDateTime.now())
						                .usuario(usuario)
						                .tipo(dto.tipo())
						                .build();

		if ( dto.proveedorId() != null) {
			Proveedores proveedor = proveedoresRepository.findById(dto.proveedorId())
							                        .orElseThrow(() -> new ResourceNotFoundException("No existe el proveedor"));
			orden.setProveedor(proveedor);
		} else {
			orden.setProveedor(null);
		}

		if (dto.clienteId() != null) {
			Clientes cliente = clientesRepository.findById(dto.clienteId())
							                   .orElseThrow(() -> new ResourceNotFoundException("No existe el cliente"));
			orden.setCliente(cliente);
		} else {
			orden.setCliente(null);
		}

		if (orden.getTipo() == TipoOrdenEnum.VENTA && orden.getProveedor() != null) {
			throw new InvalidDataException("No se puede crear una orden de venta con un proveedor asignado");
		}

		if (orden.getTipo() == TipoOrdenEnum.COMPRA && orden.getCliente() != null) {
			throw new InvalidDataException("No se puede crear una orden de compra con un cliente asignado");
		}

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

			BigDecimal subtotal = detalleOrden.getPrecio_unitario().multiply(BigDecimal.valueOf(detalle.cantidad()));
			detalleOrden.setSubtotal(subtotal);


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
						                .orElseThrow(() -> new RuntimeException("Orden no encontrada con el id: " + ordenId));

		orden.setEstado(EstadoOrdenEnum.EN_PROCESO);

		List<DetallesOrden> detalles = orden.getDetalles();
		List<Productos> productos = new ArrayList<>();

		if (orden.getTipo() == TipoOrdenEnum.COMPRA) {

			for (DetallesOrden detalle : detalles) {

				Productos producto = detalle.getProducto();
				productos.add(producto);
				producto.setProveedor(orden.getProveedor());

				Proveedores proveedor = detalle.getProducto().getProveedor();
				proveedor.setProductos(productos);

				int cantidad = detalle.getCantidad();

				MovimientoInventario movimiento = movimientoInventarioService.entrada(producto,cantidad,"Compra",orden);
				movimientoInventarioRepository.save(movimiento);
			}
		}

		if (orden.getTipo() == TipoOrdenEnum.VENTA){
			for (DetallesOrden detalle : detalles) {
				Productos producto = detalle.getProducto();
				int cantidad = detalle.getCantidad();

				MovimientoInventario movimiento = movimientoInventarioService.salida(producto,cantidad,"Venta", orden);
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

		return ordenes.map( orden -> {
			List<DetallesResponseDto> detallesResponse = orden.getDetalles()
                               .stream()
                               .map(detalle -> new DetallesResponseDto(
																			 detalle.getProducto().getNombre(),
                                       detalle.getCantidad(),
                                       detalle.getPrecio_unitario()
                               ))
                               .toList();

			return new OrdenResponseDto(
							orden.getEstado(),
							orden.getProveedor() != null ? orden.getProveedor().getNombre() : null,
							orden.getCliente() != null ? orden.getCliente().getNombre() : null,
							orden.getUsuario() != null ? orden.getUsuario().getNombre() : null,
							orden.getTipo(),
							orden.getTotal(),
							detallesResponse);

		});
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
						orden.getProveedor() != null ? orden.getProveedor().getNombre() : null,
						orden.getCliente() != null ? orden.getCliente().getNombre() : null,
						orden.getUsuario() != null ? orden.getUsuario().getNombre() : null,
						orden.getTipo(),
						orden.getTotal(),
						detallesResponse
		);
	}
}
