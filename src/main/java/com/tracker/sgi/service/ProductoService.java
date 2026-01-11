package com.tracker.sgi.service;

import com.tracker.sgi.dto.request.ProductoRequestDto;
import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.exception.InvalidDataException;
import com.tracker.sgi.exception.ResourceNotFoundException;
import com.tracker.sgi.repository.CategoriaRepository;
import com.tracker.sgi.repository.ProductoRepository;
import com.tracker.sgi.util.validations.ProductoValidate;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductoService {

	private final ProductoRepository productoRepository;
	private final CategoriaRepository categoriaRepository;

	public Productos agregarProducto(ProductoRequestDto dto) {
		// Validar que el producto no exista
		if (productoRepository.findByNombre(dto.nombre()).isPresent()) {
			throw new InvalidDataException("El producto ya existe");
		}

		Productos producto = Productos
				.builder()
				.nombre(dto.nombre())
				.precio(dto.precio())
				.stock_minimo(dto.stock_minimo()) // util para alertas
				.disponible(dto.disponible() || true)
				.categoria(categoriaRepository.findByNombre(dto.categoria()))
				.fecha_creacion(LocalDateTime.now())
				.build();

		ProductoValidate.validate(producto);
		return productoRepository.save(producto);
	}

	/**
	 * =================================================
	 * ACTUALIZAR PRODUCTOS DE FORMA PARCIAL
	 * =================================================
	 */

	public Productos actualizarPrecio(long id, BigDecimal precio) {
		Productos productoExistente = productoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));

		productoExistente.setPrecio(precio);
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
	 * ACTUALIZAR PRODUCTO
	 * =================================================
	 * 
	 * @throws AccessDeniedException
	 */

	public Productos actualizarProductoCompleto(long id, ProductoRequestDto dto) throws AccessDeniedException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		boolean esAdminOrAlmacenista = authentication.getAuthorities().stream()
				.anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")
						|| auth.getAuthority().equals("ROLE_ALMACENISTA"));

		if (!esAdminOrAlmacenista) {
			throw new AccessDeniedException("Acción no disponible para el usuario");
		}

		Productos productoExistente = productoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));

		productoExistente.setNombre(dto.nombre());
		productoExistente.setPrecio(dto.precio());
		productoExistente.setStock_actual(dto.stock_actual());
		productoExistente.setStock_minimo(dto.stock_minimo());
		productoExistente.setDisponible(dto.disponible());
		productoExistente.setCategoria(dto.categoria());

		ProductoValidate.validate(productoExistente);

		return productoRepository.save(productoExistente);
	}

	/**
	 * =================================================
	 *OBTENER PRODUCTOS
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
	 * ELIMINAR PRODUCTO
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
