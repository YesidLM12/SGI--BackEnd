package com.tracker.sgi.entities;

import com.tracker.sgi.util.enums.RolEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Rol {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private RolEnum rol;

	@OneToMany(mappedBy = "rol")
	private List<Usuarios> usuario;
}
