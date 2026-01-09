package com.tracker.sgi.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "productos")
public class Productos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private BigDecimal precio_compra;
    private BigDecimal precio_venta;
    private int stock_actual;
    private boolean disponible;
    private LocalDate fecha_creacion;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categorias categoria;

    @ManyToOne
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedores proveedor;

    @OneToMany(mappedBy = "producto")
    private List<MovimientoInventario> movimientos;   
}
