package com.tracker.sgi.dto.response;


import java.math.BigDecimal;
import java.util.List;

import com.tracker.sgi.util.enums.EstadoOrdenEnum;
import com.tracker.sgi.util.enums.TipoOrdenEnum;

public record OrdenResponseDto(
				EstadoOrdenEnum estado,
				Long proveedorId,
				Long clienteId,
				Long usuarioId,
				TipoOrdenEnum tipo,
				BigDecimal total,
				List<DetallesResponseDto> detalles
) {
}
