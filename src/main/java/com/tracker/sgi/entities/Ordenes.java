package com.tracker.sgi.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.tracker.sgi.exception.BusinessRuleException;
import com.tracker.sgi.util.enums.EstadoOrdenEnum;
import com.tracker.sgi.util.enums.TipoOrdenEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.CascadeType;
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
public class Ordenes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private TipoOrdenEnum tipo;

	@Enumerated(EnumType.STRING)
	private EstadoOrdenEnum estado;
	private LocalDateTime fecha;
	private BigDecimal total;

	@ManyToOne
	@JoinColumn(name = "proveedor_id")
	private Proveedores proveedor;

	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Clientes cliente;

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuarios usuario;

	@OneToMany(mappedBy = "orden")
	private List<MovimientoInventario> movimientos;

	@OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DetallesOrden> detalles;

	public BigDecimal calcularTotal() {
		this.total = this.detalles.stream()
						             .map(d -> d.getPrecio_unitario()
										                       .multiply(BigDecimal.valueOf(d.getCantidad())))
						             .reduce(BigDecimal.ZERO, BigDecimal::add);
		return total;
	}

	public void cancelar() {
		if (estado == EstadoOrdenEnum.PENDIENTE) {
			estado = EstadoOrdenEnum.CANCELADO;
			return;
		}

		if (estado == EstadoOrdenEnum.EN_PROCESO) {
			movimientos.clear();
			estado = EstadoOrdenEnum.CANCELADO;
			return;
		}

		throw new BusinessRuleException("La orden no se puede cancelar en estado " + estado);
	}


}
