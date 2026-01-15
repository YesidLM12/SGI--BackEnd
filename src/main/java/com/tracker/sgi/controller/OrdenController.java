package com.tracker.sgi.controller;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.sgi.dto.request.OrdenRequestDto;
import com.tracker.sgi.entities.Ordenes;
import com.tracker.sgi.service.OrdenService;
import com.tracker.sgi.util.enums.EstadoOrdenEnum;
import com.tracker.sgi.util.enums.TipoOrdenEnum;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    @PostMapping("/comprar")
    public Ordenes comprarOrden(@RequestBody OrdenRequestDto dto) throws AccessDeniedException {
        return ordenService.comprarOrden(dto);
    }

    @PostMapping("/venta")
    public Ordenes VentaOrden(@RequestBody OrdenRequestDto dto) throws AccessDeniedException {
        return ordenService.VentaOrden(dto);
    }

    @GetMapping
    public Page<Ordenes> obtenerTodasLasOrdenes(Pageable pageable) {
        return ordenService.obtenerTodasLasOrdenes(pageable);
    }

    @GetMapping("/cliente/{clienteId}")
    public Page<Ordenes> obtenerOrdenesPorCliente(@PathVariable Long clienteId, Pageable pageable) {
        return ordenService.obtenerOrdenesPorCliente(clienteId, pageable);
    }

    @GetMapping("/proveedor/{proveedorId}")
    public Page<Ordenes> obtenerOrdenesPorProveedor(@PathVariable Long proveedorId, Pageable pageable) {
        return ordenService.obtenerOrdenesPorProveedor(proveedorId, pageable);
    }

    @GetMapping("/tipo/{tipo}")
    public Page<Ordenes> obtenerOrdenesPorTipo(@PathVariable TipoOrdenEnum tipo, Pageable pageable) {
        return ordenService.obtenerOrdenesPorTipo(tipo, pageable);
    }

    @GetMapping("/estado/{estado}")
    public Page<Ordenes> obtenerOrdenesPorEstado(@PathVariable EstadoOrdenEnum estado, Pageable pageable) {
        return ordenService.obtenerOrdenesPorEstado(estado, pageable);
    }

    @GetMapping("/fecha/{fecha}")
    public Page<Ordenes> obtenerOrdenesPorFecha(@PathVariable LocalDateTime fecha, Pageable pageable) {
        return ordenService.obtenerOrdenesPorFecha(fecha, pageable);
    }

}
