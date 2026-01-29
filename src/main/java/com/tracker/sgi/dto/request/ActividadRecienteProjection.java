package com.tracker.sgi.dto.request;

import java.time.LocalDateTime;

public interface ActividadRecienteProjection {
	Long getMovimientoId();
	String getTipoMovimiento();
	Integer getCantidad();
	LocalDateTime getFecha();
	String getUsuario();
}
