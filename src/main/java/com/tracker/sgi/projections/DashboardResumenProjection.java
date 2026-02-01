package com.tracker.sgi.projections;

public interface DashboardResumenProjection {
	Long getTotalVentas();
	Long getTotalCompras();
	Long getOrdenesPendientes();
	Long getProductosStockBajo();
}
