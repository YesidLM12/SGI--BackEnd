package com.tracker.sgi.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "proveedores")
public class Proveedores {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String nit_rut;
	@Column(length = 100)
	private String nombre;
	private String telefono;
	@Email(message = "El email debe ser válido", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
	private String email;
	private String direccion;

	@OneToMany(mappedBy = "proveedor")
	private List<Productos> productos;

	@OneToMany(mappedBy = "proveedor")
	@JsonIgnore
	private List<Ordenes> ordenes;
}
