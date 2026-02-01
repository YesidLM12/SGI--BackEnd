package com.tracker.sgi.controller;

import com.tracker.sgi.dto.response.DashboardResponseDto;
import com.tracker.sgi.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
	private final DashboardService dashboardService;

	@GetMapping
	public DashboardResponseDto obtenerDashboard(
					@RequestParam LocalDateTime fechaInicio,
					@RequestParam LocalDateTime fechaFin
					){
		return dashboardService.obtenerDashboard(fechaInicio, fechaFin);
	}
}