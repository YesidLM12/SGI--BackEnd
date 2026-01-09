package com.tracker.sgi.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    private String nit_rut;
    private String razon_social;
    private String contacto_nombre;
    private String contacto_telefono;
    private String contacto_email;
    private String direccion;

    @OneToMany(mappedBy = "proveedor")
    private List<Productos> productos;
    
    @OneToMany(mappedBy = "proveedor")
    private List<Ordenes> ordenes;
}
