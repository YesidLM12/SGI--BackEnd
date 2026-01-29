package com.tracker.sgi.entities;

import java.util.List;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "usuarios")
public class Usuarios {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;
	@Email
	@Column(unique = true)
	private String email;
	private String password;
	private Boolean activo;

	@ManyToOne
	@JoinTable(name = "usuario_rol", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "rol_id"))
	private Rol rol;

	@OneToMany(mappedBy = "usuario")
	private List<Ordenes> ordenes;

	@OneToMany(mappedBy = "usuario")
	private List<MovimientoInventario> movimientos;
}
