package com.tracker.sgi.entities;

import com.tracker.sgi.util.enums.RolEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "roles")
public class Rol {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private RolEnum rol;

	public Rol(Long id, RolEnum rol) {
		this.id = id;
		this.rol = rol;
	}

	@OneToMany(mappedBy = "rol")
	private List<Usuarios> usuario;
}
