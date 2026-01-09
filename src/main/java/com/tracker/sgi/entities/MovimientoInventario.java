package com.tracker.sgi.entities;

import java.time.LocalDate;

import com.tracker.sgi.util.enums.TipoMovimientoEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
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
@Table(name = "movimiento_inventario")
public class MovimientoInventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Productos producto;

    @Enumerated(EnumType.STRING)
    private TipoMovimientoEnum tipo_movimiento;
    private int cantidad;
    private String motivo;


    @ManyToOne
    @JoinColumn(name = "orden_id")
    private Ordenes orden;

    private LocalDate fecha_movimiento;
    private int stock_resultante;

}
