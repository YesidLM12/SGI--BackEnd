package com.tracker.sgi.dto.request;

public interface DashboardResumenProjection {
	Long getTotalVentas();
	Long getTotalCompras();
	Long getOrdenesPendientes();
	Long getProductosStockBajo();
}
