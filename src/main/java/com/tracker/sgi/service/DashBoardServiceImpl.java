package com.tracker.sgi.service;

import com.tracker.sgi.dto.request.*;
import com.tracker.sgi.dto.response.DashboardResponseDto;
import com.tracker.sgi.exception.InvalidDataException;
import com.tracker.sgi.projections.DashboardResumenProjection;
import com.tracker.sgi.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashBoardServiceImpl implements DashboardService {
	private final DashboardRepository dashboardRepository;

	@Override
	public DashboardResponseDto obtenerDashboard(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
		if (fechaInicio.isAfter(fechaFin)) {
			throw new InvalidDataException("Rango de fecha ingresado no válido. " + fechaInicio + " e " + fechaFin);
		}

		if (fechaInicio == null) {
			fechaInicio = LocalDateTime.now();
		}

		if (fechaFin == null) {
			fechaFin = fechaInicio.minusDays(30);
		}

		DashboardResumenProjection resumenProjection = dashboardRepository.obtenerResumen(fechaInicio, fechaFin);

		DashboardResumenDto resumen = new DashboardResumenDto(
						resumenProjection.getTotalVentas(),
						resumenProjection.getTotalCompras(),
						resumenProjection.getOrdenesPendientes(),
						resumenProjection.getProductosStockBajo()
		);

		List<MovimientoMensualDto> movimientosMensuales = dashboardRepository.obtenerMovimientoMensual(fechaInicio,fechaFin)
              .stream()
              .map(p -> new MovimientoMensualDto(
											p.getPeriodo(),
			                p.getTotalEntradas(),
			                p.getTotalSalidas()
              ))
              .toList();

		List<ProductoTopDto> productosTop = dashboardRepository.obtenerProductosTop(fechaInicio, fechaFin, 5)
              .stream()
              .map(p -> new ProductoTopDto(
											p.getProductoId(),
				              p.getNombreProducto(),
				              p.getCantidadVendida()
              ))
              .toList();

		List<AlertaStockDto> alertasStock = dashboardRepository.obtenerAlertasStock()
           .stream()
           .map(p -> new AlertaStockDto(
                   p.getNombreProducto(),
                   p.getStockActual(),
                   p.getStockMinimo(),
                   p.getEstado()
           ))
           .toList();

		List<ActividadRecienteDto> actividadReciente = dashboardRepository.obtenerActividadReciente()
               .stream()
                .map(p -> new ActividadRecienteDto(
												p.getMovimientoId(),
				                p.getTipoMovimiento(),
				                p.getCantidad(),
				                p.getFecha(),
				                p.getUsuario()
                ))
                .toList();

		return new DashboardResponseDto(
						resumen,
						movimientosMensuales,
						productosTop,
						alertasStock,
						actividadReciente
		);
	}


}
