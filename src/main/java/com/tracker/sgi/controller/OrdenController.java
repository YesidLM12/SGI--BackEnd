package com.tracker.sgi.controller;

import java.util.Map;

import com.tracker.sgi.dto.response.OrdenResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.tracker.sgi.dto.request.OrdenRequestDto;
import com.tracker.sgi.service.OrdenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> crearOrden(@RequestBody OrdenRequestDto ordenRequestDto) {
        ordenService.crearOrden(ordenRequestDto);
        return Map.of("message", "Orden realizada exitosamente.");
    }

    @PostMapping("/confirmar/{id}")
    public Map<String, String> confirmarOrden(@PathVariable Long id){
        ordenService.confirmarOrden(id);
        return Map.of("message", "Orden en proceso.");
    }

    @PostMapping("/cancelar/{id}")
    public Map<String, String> cancelarOrden(@PathVariable Long id){
        ordenService.cancelarOrden(id);
        return Map.of("message", "Orden cancelada exitosamente.");
    }

    @GetMapping
    public Page<OrdenResponseDto> getOrdenes(Pageable pageable){
       return ordenService.obtenerOrdenes(pageable);
    }

    @GetMapping("/{id}")
    public OrdenResponseDto getOrden(@PathVariable Long id){
        return  ordenService.obtenerOrdenPorId(id);
    }

}
