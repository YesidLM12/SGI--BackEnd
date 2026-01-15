package com.tracker.sgi.service;

import com.tracker.sgi.entities.MovimientoInventario;
import com.tracker.sgi.entities.Productos;
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

	public MovimientoInventario entrada(Productos producto, int cantidad, String motivo) {
		MovimientoInventario movimiento = MovimientoInventario
				.builder()
				.producto(producto)
				.tipo_movimiento(TipoMovimientoEnum.ENTRADA)
				.cantidad(cantidad)
				.motivo(motivo != null ? motivo : "Entrada")
				.fecha_movimiento(LocalDate.now())
				.stock_resultante(productoRepository.calcularStockActual(producto.getId()))
				.build();

		movimientoInventarioRepository.save(movimiento);

		producto.setStock_actual(movimiento.getStock_resultante());
		productoRepository.save(producto);
		return movimiento;
	}


	public MovimientoInventario salida(Productos producto, int cantidad, String motivo) {

		if (producto.getStock_actual() < cantidad) {
			throw new InvalidStockMovementException("No hay suficiente stock");
		}

		MovimientoInventario movimiento = MovimientoInventario
				.builder()
				.producto(producto)
				.tipo_movimiento(TipoMovimientoEnum.SALIDA)
				.cantidad(cantidad)
				.motivo(motivo != null ? motivo : "Salida")
				.fecha_movimiento(LocalDate.now())
				.stock_resultante(productoRepository.calcularStockActual(producto.getId()))
				.build();
		return movimientoInventarioRepository.save(movimiento);
	}


	public MovimientoInventario ajustePositivo(Productos producto, int cantidad, String motivo) {

		MovimientoInventario movimiento = MovimientoInventario
				.builder()
				.producto(producto)
				.tipo_movimiento(TipoMovimientoEnum.AJUSTE_POSITIVO)
				.cantidad(cantidad)
				.motivo(motivo != null ? motivo : "Ajuste Positivo")
				.fecha_movimiento(LocalDate.now())
				.stock_resultante(productoRepository.calcularStockActual(producto.getId()))
				.build();

		producto.setStock_actual(movimiento.getStock_resultante());
		productoRepository.save(producto);

		return movimientoInventarioRepository.save(movimiento);
	}


	public MovimientoInventario ajusteNegativo(Productos producto, int cantidad, String motivo) {

		if (producto.getStock_actual() < cantidad) {
			throw new InvalidStockMovementException("No hay suficiente stock");
		}

		MovimientoInventario movimiento = MovimientoInventario
				.builder()
				.producto(producto)
				.tipo_movimiento(TipoMovimientoEnum.AJUSTE_NEGATIVO)
				.cantidad(cantidad)
				.motivo(motivo != null ? motivo : "Ajuste Negativo")
				.fecha_movimiento(LocalDate.now())
				.stock_resultante(productoRepository.calcularStockActual(producto.getId()))
				.build();

		producto.setStock_actual(movimiento.getStock_resultante());
		productoRepository.save(producto);

		return movimientoInventarioRepository.save(movimiento);
	}
}
