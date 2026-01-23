package com.tracker.sgi.dto.response;


import java.util.List;

public record ProveedorResponseDto(
				String nit_rut,
				String nombre,
				String telefono,
				String email,
				String direccion,
				List<String> productos
) {
}
