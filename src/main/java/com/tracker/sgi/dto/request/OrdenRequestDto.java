package com.tracker.sgi.dto.request;

import java.util.List;

import com.tracker.sgi.util.enums.TipoOrdenEnum;

public record OrdenRequestDto(
        TipoOrdenEnum tipo,
        Long proveedorId,
        Long clienteId,
        Long usuarioId,
        List<DetallesRequestDto> detalles) {

}
