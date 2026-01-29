package com.tracker.sgi.service;

import com.tracker.sgi.dto.response.DashboardResponseDto;

import java.time.LocalDateTime;

public interface DashboardService {
	DashboardResponseDto obtenerDashboard(
					LocalDateTime fechaInicio,
					LocalDateTime fechaFin
	);
}
