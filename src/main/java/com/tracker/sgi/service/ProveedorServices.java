package com.tracker.sgi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tracker.sgi.dto.request.ProveedorRequestDto;
import com.tracker.sgi.entities.Proveedores;
import com.tracker.sgi.exception.InvalidDataException;
import com.tracker.sgi.repository.ProveedoresRepository;
import com.tracker.sgi.util.validations.ProveedorValidate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProveedorServices {

    private final ProveedoresRepository proveedoresRepository;

    public Proveedores agregarProveedor(ProveedorRequestDto dto) {

        if (proveedoresRepository.findAll().stream().anyMatch(p -> p.getNit_rut().equals(dto.nit_rut()))) {
            throw new InvalidDataException("El proveedor ya existe");
        }

        ProveedorValidate.validate(dto);

        Proveedores proveedor = Proveedores.builder()
                .nit_rut(dto.nit_rut())
                .nombre(dto.nombre())
                .telefono(dto.telefono())
                .email(dto.email())
                .direccion(dto.direccion())
                .build();

        return proveedoresRepository.save(proveedor);
    }

    public Page<Proveedores> listarProveedores(Pageable pageable) {
        return proveedoresRepository.findAll(pageable);
    }

    public Proveedores obtenerProveedor(Long id) {
        return proveedoresRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("El proveedor no existe"));
    }

    public Proveedores actualizarProveedor(Long id, ProveedorRequestDto dto) {
        Proveedores proveedorExistente = obtenerProveedor(id);

        ProveedorValidate.validate(dto);

        proveedorExistente.setNit_rut(dto.nit_rut());
        proveedorExistente.setNombre(dto.nombre());
        proveedorExistente.setTelefono(dto.telefono());
        proveedorExistente.setEmail(dto.email());
        proveedorExistente.setDireccion(dto.direccion());

        return proveedoresRepository.save(proveedorExistente);
    }
    
    public void eliminarProveedor(Long id) {
        Proveedores proveedorExistente = obtenerProveedor(id);
        proveedoresRepository.delete(proveedorExistente);
    }

}
