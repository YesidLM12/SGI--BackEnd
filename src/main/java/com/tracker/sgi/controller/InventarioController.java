package com.tracker.sgi.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.sgi.dto.request.InventarioRequestDto;
import com.tracker.sgi.dto.request.ProductoRequestDto;
import com.tracker.sgi.service.ProductoService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventario")
public class InventarioController {
    private final ProductoService productoService;

    @PostMapping("/entrada")
    public Map<String, String> entradaProducto(@RequestBody ProductoRequestDto dto) {
        productoService.entradaProducto(dto);
        return Map.of("message", "Producto agregado correctamente.");
    }

    @PostMapping("/salida")
    public Map<String, String> salidaProducto(@RequestBody InventarioRequestDto dto) {
        productoService.salidaProducto(dto.nombre(), dto.cantidad(), "Salida por venta");
        return Map.of("message", "Producto retirado correctamente.");
    }

    @PostMapping("/ajuste-positivo")
    public Map<String, String> ajustePositivo(@RequestBody InventarioRequestDto dto) {
        productoService.ajustePositivoProducto(dto.nombre(), dto.cantidad(), dto.motivo());
        return Map.of("message", "Ajuste realizado correctamente.");
    }

    @PostMapping("/ajuste-negativo")
    public Map<String, String> ajusteNegativo(@RequestBody InventarioRequestDto dto) {
        productoService.ajusteNegativoProducto(dto.nombre(), dto.cantidad(), dto.motivo());
        return Map.of("message", "Ajuste realizado correctamente.");
    }

}
