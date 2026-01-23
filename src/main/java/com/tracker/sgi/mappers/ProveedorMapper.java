package com.tracker.sgi.mappers;

import com.tracker.sgi.dto.request.ProveedorRequestDto;
import com.tracker.sgi.dto.response.ProveedorResponseDto;
import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.entities.Proveedores;
import org.springframework.stereotype.Component;

@Component
public class ProveedorMapper {

	public static Proveedores toEntity(ProveedorRequestDto proveedores) {

		return Proveedores.builder()
				.nit_rut(proveedores.nit_rut())
				.nombre(proveedores.nombre())
				.telefono(proveedores.telefono())
				.email(proveedores.email())
				.direccion(proveedores.direccion())
				.build();

	}

	public static ProveedorResponseDto toDto(Proveedores proveedores) {

		return new ProveedorResponseDto(
				proveedores.getNit_rut(),
				proveedores.getNombre(),
				proveedores.getTelefono(),
				proveedores.getEmail(),
				proveedores.getDireccion(),
				proveedores.getProductos().stream().map(Productos::getNombre).toList());
	}
}
