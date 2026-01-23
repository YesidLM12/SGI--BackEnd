package com.tracker.sgi.service;

import com.tracker.sgi.entities.MovimientoInventario;
import com.tracker.sgi.entities.Ordenes;
import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.exception.BusinessRuleException;
import com.tracker.sgi.exception.InvalidStockMovementException;
import com.tracker.sgi.repository.MovimientoInventarioRepository;
import com.tracker.sgi.repository.ProductoRepository;
import com.tracker.sgi.util.enums.TipoMovimientoEnum;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MovimientosService {

	private final ProductoRepository productoRepository;
	private final MovimientoInventarioRepository movimientoInventarioRepository;

	public MovimientoInventario entrada(Productos producto, int cantidad, String motivo, Ordenes orden) {
		Integer stockActual = obtenerStockActual(producto.getId());

		if (stockActual == null) {
			stockActual = 0;
		}

		MovimientoInventario movimiento = MovimientoInventario
				.builder()
				.producto(producto)
				.tipo_movimiento(TipoMovimientoEnum.ENTRADA)
				.cantidad(cantidad)
				.motivo(motivo != null ? motivo : "Entrada")
				.fecha_movimiento(LocalDate.now())
				.stock_resultante(stockActual + cantidad)
        .orden(orden)
				.build();

		movimientoInventarioRepository.save(movimiento);

		producto.setStock_actual(obtenerStockActual(producto.getId()));

		productoRepository.save(producto);
		return movimiento;
	}


	public MovimientoInventario salida(Productos producto, int cantidad, String motivo, Ordenes orden) {

		Integer stockActual = obtenerStockActual(producto.getId());

		if (stockActual < cantidad) {
			throw new BusinessRuleException("Stock insuficiente para el producto: " + producto.getNombre()
							                                + ". Stock actual: " + stockActual
							                                + ", solicitado: " + cantidad);
		}

		MovimientoInventario movimiento = MovimientoInventario
				.builder()
				.producto(producto)
				.tipo_movimiento(TipoMovimientoEnum.SALIDA)
				.cantidad(cantidad)
				.motivo(motivo != null ? motivo : "Salida")
				.fecha_movimiento(LocalDate.now())
				.stock_resultante(stockActual - cantidad)
        .orden(orden)
				.build();


		producto.setStock_actual(obtenerStockActual(producto.getId()));
		productoRepository.save(producto);
		return movimientoInventarioRepository.save(movimiento);
	}

	public void ajustePositivo(Productos producto, int cantidad, String motivo) {

		Integer stockActual = obtenerStockActual(producto.getId());
		if (stockActual == null) {
			stockActual = 0;
		}


		MovimientoInventario movimiento = MovimientoInventario
				.builder()
				.producto(producto)
				.tipo_movimiento(TipoMovimientoEnum.AJUSTE_POSITIVO)
				.cantidad(cantidad)
				.motivo(motivo != null ? motivo : "Ajuste Positivo")
				.fecha_movimiento(LocalDate.now())
				.stock_resultante(stockActual + cantidad)
				.build();

		producto.setStock_actual(obtenerStockActual(producto.getId()));
		productoRepository.save(producto);

		movimientoInventarioRepository.save(movimiento);
	}

	public void ajusteNegativo(Productos producto, int cantidad, String motivo) {

		Integer stockActual = obtenerStockActual(producto.getId());
		if (stockActual < cantidad) {
			throw new InvalidStockMovementException("Stock insuficiente para el producto: " + producto.getNombre()
							                                        + ". Stock actual: " + stockActual
							                                        + ", solicitado: " + cantidad);
		}

		MovimientoInventario movimiento = MovimientoInventario
				.builder()
				.producto(producto)
				.tipo_movimiento(TipoMovimientoEnum.AJUSTE_NEGATIVO)
				.cantidad(cantidad)
				.motivo(motivo != null ? motivo : "Ajuste Negativo")
				.fecha_movimiento(LocalDate.now())
				.stock_resultante(producto.getStock_actual() - cantidad)
				.build();

		producto.setStock_actual(obtenerStockActual(producto.getId()));
		productoRepository.save(producto);

		movimientoInventarioRepository.save(movimiento);
	}

	public Integer obtenerStockActual(Long productoId){
		return productoRepository.calcularStockActual(productoId);
	}
}
