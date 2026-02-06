package com.tracker.sgi.service;

import com.tracker.sgi.entities.MovimientoInventario;
import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.exception.InvalidStockMovementException;
import com.tracker.sgi.repository.MovimientoInventarioRepository;
import com.tracker.sgi.repository.ProductoRepository;
import com.tracker.sgi.util.enums.TipoMovimientoEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {
	@Mock
	private ProductoRepository productoRepository;

	@Mock
	private MovimientoInventarioRepository movimientoInventarioRepository;

	@Spy
	@InjectMocks
	private InventarioService inventarioService;
	@Test
	void debeRegistrarSalidaCuandoHayStockSuficiente() {
		Productos producto = new Productos();
		producto.setId(1L);
		producto.setNombre("Teclado");
		producto.setStock_actual(10);

		doReturn(10).when(inventarioService).obtenerStockActual(1L);


		MovimientoInventario movimiento = inventarioService.registrarMovimiento(producto, TipoMovimientoEnum.SALIDA,3);

		assertNotNull(movimiento);
		assertEquals(7, movimiento.getStock_resultante());

		verify(movimientoInventarioRepository).save(any());
		verify(productoRepository).save(producto);
	}

	@Test
	void debeLanzarExepcionSiStockInsuficiente() {
		Productos producto = new Productos();
		producto.setId(1L);
		producto.setNombre("Teclado");
		producto.setStock_actual(3);

		doReturn(3).when(inventarioService).obtenerStockActual(1L);

		assertThrows(
			InvalidStockMovementException.class,
			() ->  inventarioService.registrarMovimiento(producto, TipoMovimientoEnum.SALIDA, 5));
		
		verify(productoRepository, never()).save(producto);
		verify(movimientoInventarioRepository, never()).save(any());
	}
}
