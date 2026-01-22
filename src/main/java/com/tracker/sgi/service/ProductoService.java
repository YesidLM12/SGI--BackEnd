package com.tracker.sgi.service;

import com.tracker.sgi.dto.request.ActualizarProductoRequestDto;
import com.tracker.sgi.dto.request.ProductoRequestDto;
import com.tracker.sgi.dto.response.ProductoResponseDto;
import com.tracker.sgi.entities.Categorias;
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

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductoService {

	private final ProductoRepository productoRepository;
	private final CategoriaRepository categoriaRepository;

	public ProductoResponseDto agregarProducto(ProductoRequestDto dto) {
		// Validar que el producto no exista
		if (productoRepository.findByNombre(dto.nombre()).isPresent()) {
			throw new InvalidDataException("El producto ya existe");
		}

		Categorias categoria = categoriaRepository.findById(dto.categoriaId())
				.orElseThrow(() -> new ResourceNotFoundException("La categoria no existe"));

		Productos producto = Productos
				.builder()
				.nombre(dto.nombre())
				.precio(dto.precio())
				.stock_minimo(dto.stock_minimo()) // util para alertas
				.disponible(true)
				.categoria(categoria)
				.fecha_creacion(LocalDateTime.now())
				.build();

		ProductoValidate.validate(producto);
		productoRepository.save(producto);

		return new ProductoResponseDto(
						producto.getNombre(),
						producto.getPrecio(),
						producto.getStock_actual(),
						producto.getStock_minimo(),
						producto.getCategoria().getNombre(),
						producto.getFecha_creacion()
		);
	}

	/**
	 * =================================================
	 * ACTUALIZAR PRODUCTOS DE FORMA PARCIAL
	 * =================================================
	 */

	public Productos actualizarProductoParcial(long id, ActualizarProductoRequestDto dto) {
		Productos productoExistente = productoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));

		if (dto.precio() != null) {
			productoExistente.setPrecio(dto.precio());
		}
		if (dto.stock_minimo() != 0) {
			productoExistente.setStock_minimo(dto.stock_minimo());
		}
		if (dto.disponible() != productoExistente.isDisponible()) {
			productoExistente.setDisponible(dto.disponible());
		}
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
		productoExistente.setStock_minimo(dto.stock_minimo());
		productoExistente.setDisponible(true);

		Categorias categoria = categoriaRepository.findById(dto.categoriaId())
				.orElseThrow(() -> new ResourceNotFoundException("La categoria no existe"));
		productoExistente.setCategoria(categoria);

		ProductoValidate.validate(productoExistente);

		return productoRepository.save(productoExistente);
	}

	/**
	 * =================================================
	 * OBTENER PRODUCTOS
	 * =================================================
	 */
	public Page<Productos> obtenerTodosLosProductos(Pageable pageable) {
		return productoRepository.findAll(pageable);
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

	public void eliminarProductoPorId(Long id) {
		Productos productoExistente = productoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("El producto no existe"));

		productoRepository.delete(productoExistente);
	}
}
