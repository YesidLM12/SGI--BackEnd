package com.tracker.sgi.service;

import com.tracker.sgi.dto.request.ProductoRequestDto;
import com.tracker.sgi.entities.MovimientoInventario;
import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.exception.InvalidDataException;
import com.tracker.sgi.exception.ResourceNotFoundException;
import com.tracker.sgi.repository.ProductoRepository;
import com.tracker.sgi.util.validations.ProductoValidate;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ProductoService {
	private final ProductoRepository productoRepository;
	private final MovimientosService movimientosService;

	/**
	 * =================================================
	 * METODOS PARA GESTIONAR INVENTARIO
	 * =================================================
	 */

	public Productos entradaProducto(ProductoRequestDto dto) {
		// Validar que el producto no exista
		if (productoRepository.findByNombre(dto.nombre()).isPresent()) {
			throw new InvalidDataException("El producto ya existe");
		}

		Productos producto = Productos
				.builder()
				.nombre(dto.nombre())
				.precio_compra(dto.precio_compra())
				.precio_venta(dto.precio_venta())
				.stock_minimo(dto.stock_minimo()) // util para alertas
				.disponible(dto.disponible())
				.categoria(dto.categoria())
				.fecha_creacion(LocalDate.now())
				.build();

		MovimientoInventario movimiento = movimientosService.entrada(producto, dto.stock_actual(), "Entrada inicial");

		producto.setStock_actual(movimiento.getStock_resultante());
		ProductoValidate.validate(producto);

		return productoRepository.save(producto);

		/*
		 * La entrada de productos debe relacionarse
		 * con la compra (proveedor).
		 */
	}

	public Productos salidaProducto(String nombreProducto, int cantidad, String motivo) {
		// Verificar existencia del producto
		Productos productoExistente = productoRepository.findByNombre(nombreProducto)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));

		// Validar que el stock sea suficiente
		if (productoExistente.getStock_actual() < cantidad) {
			throw new InvalidDataException("No hay suficiente stock");
		}

		// Realizar el movimiento
		MovimientoInventario movimiento = movimientosService.Salida(productoExistente, cantidad, motivo);

		// Establecer el stock actual después del movimiento
		productoExistente.setStock_actual(movimiento.getStock_resultante());

		// Guardar el producto con el cambio de stock
		return productoRepository.save(productoExistente);

		/*
		 * La salida de productos debe relacionarse
		 * con la venta (cliente).
		 */
	}

	public Productos ajustePositivoProducto(String nombreProducto, int cantidad, String motivo) {
		// Verificar existencia del producto
		Productos productoExistente = productoRepository.findByNombre(nombreProducto)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));

		// Realizar el movimiento
		MovimientoInventario movimiento = movimientosService.ajustePositivo(productoExistente, cantidad, motivo);

		// Establecer el stock actual después del movimiento
		productoExistente.setStock_actual(movimiento.getStock_resultante());

		// Guardar el producto con el cambio de stock
		return productoRepository.save(productoExistente);
	}

	public Productos ajusteNegativoProducto(String nombreProducto, int cantidad, String motivo) {
		// Verificar existencia del producto
		Productos productoExistente = productoRepository.findByNombre(nombreProducto)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));

		// Realizar el movimiento
		MovimientoInventario movimiento = movimientosService.ajusteNegativo(productoExistente, cantidad, motivo);

		// Establecer el stock actual después del movimiento
		productoExistente.setStock_actual(movimiento.getStock_resultante());

		// Guardar el producto con el cambio de stock
		return productoRepository.save(productoExistente);
	}

	/**
	 * =================================================
	 * METODOS PARA ACTUALIZAR PRODUCTOS DE FORMA PARCIAL
	 * =================================================
	 */

	public Productos actualizarPrecioCompra(long id, BigDecimal precioCompra) {
		Productos productoExistente = productoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));

		productoExistente.setPrecio_compra(precioCompra);
		ProductoValidate.validate(productoExistente);
		return productoRepository.save(productoExistente);
	}

	public Productos actualizarPrecioVenta(long id, BigDecimal precioVenta) {
		Productos productoExistente = productoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));

		productoExistente.setPrecio_venta(precioVenta);
		ProductoValidate.validate(productoExistente);
		return productoRepository.save(productoExistente);
	}

	public Productos actualizarStockMinimo(long id, int stockMinimo) {
		Productos productoExistente = productoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));

		productoExistente.setStock_minimo(stockMinimo);
		ProductoValidate.validate(productoExistente);
		return productoRepository.save(productoExistente);
	}

	public Productos actualizarDisponibilidad(long id, boolean disponible) {
		Productos productoExistente = productoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));

		productoExistente.setDisponible(disponible);
		return productoRepository.save(productoExistente);
	}

	/**
	 * =================================================
	 * METODO PARA REEMPLAZAR PRODUCTOS DE FORMA COMPLETA
	 * =================================================
	 */

	public Productos actualizarProductoCompleto(long id, ProductoRequestDto dto) {
		Productos productoExistente = productoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));

		productoExistente.setNombre(dto.nombre());
		productoExistente.setPrecio_compra(dto.precio_compra());
		productoExistente.setPrecio_venta(dto.precio_venta());
		productoExistente.setStock_actual(dto.stock_actual());
		productoExistente.setStock_minimo(dto.stock_minimo());
		productoExistente.setDisponible(dto.disponible());
		productoExistente.setCategoria(dto.categoria());

		ProductoValidate.validate(productoExistente);

		return productoRepository.save(productoExistente);
	}

	/**
	 * =================================================
	 * METODOS PARA OBTENER PRODUCTOS
	 * =================================================
	 */
	public Page<Productos> obtenerTodosLosProductos(Pageable pageable) {
		return productoRepository.findAll(pageable);
	}

	public Productos obtenerProductoPorNombre(String nombre) {
		return productoRepository.findByNombre(nombre)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));
	}

	public Productos obtenerProductoPorId(Long id) {
		return productoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));
	}

	/**
	 * =================================================
	 * METODOS PARA ELIMINAR PRODUCTOS
	 * =================================================
	 */
	public void eliminarProductoPorNombre(String nombreProducto) {
		Productos productoExistente = productoRepository.findByNombre(nombreProducto)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));

		productoRepository.delete(productoExistente);
	}

	public void eliminarProductoPorId(Long id) {
		Productos productoExistente = productoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));

		productoRepository.delete(productoExistente);
	}
}
