package com.tracker.sgi.dto.request;

import com.tracker.sgi.util.enums.EstadoStockEnum;

public interface AlertasStockProjection {
	Long getProductoId();
	String getNombreProducto();
	Integer getStockActual();
	Integer getStockMinimo();
	EstadoStockEnum getEstado();
}
