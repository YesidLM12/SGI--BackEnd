package com.tracker.sgi.service;

import com.tracker.sgi.dto.response.ProveedorResponseDto;
import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.mappers.ProveedorMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tracker.sgi.dto.request.ProveedorRequestDto;
import com.tracker.sgi.entities.Proveedores;
import com.tracker.sgi.exception.InvalidDataException;
import com.tracker.sgi.repository.ProveedoresRepository;
import com.tracker.sgi.util.validations.ProveedorValidate;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProveedorServices {

    private final ProveedoresRepository proveedoresRepository;

    public void agregarProveedor(ProveedorRequestDto dto) {

        if (proveedoresRepository.findAll().stream().anyMatch(p -> p.getNit_rut().equals(dto.nit_rut()))) {
            throw new InvalidDataException("El proveedor ya existe");
        }

        ProveedorValidate.validate(dto);
        Proveedores proveedor = ProveedorMapper.toEntity(dto);
        proveedoresRepository.save(proveedor);
    }

    public Page<ProveedorResponseDto> listarProveedores(Pageable pageable) {
        Page<Proveedores> proveedores = proveedoresRepository.findAll(pageable);

        return proveedores.map(proveedor -> new ProveedorResponseDto(
                proveedor.getNit_rut(),
                proveedor.getNombre(),
                proveedor.getTelefono(),
                proveedor.getEmail(),
                proveedor.getDireccion(),
                proveedor.getProductos().stream().map(Productos::getNombre).toList()));
    }

    public ProveedorResponseDto obtenerProveedor(Long id) {
        Proveedores proveedor = proveedoresRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("El proveedor no existe"));
        return new ProveedorResponseDto(
                proveedor.getNit_rut(),
                proveedor.getNombre(),
                proveedor.getTelefono(),
                proveedor.getEmail(),
                proveedor.getDireccion(),
                proveedor.getProductos().stream().map(Productos::getNombre).toList()
        );
    }

    public Proveedores actualizarProveedor(Long id, ProveedorRequestDto dto) {
        Proveedores proveedorExistente = proveedoresRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("El proveedor no existe"));

        ProveedorValidate.validate(dto);

        proveedorExistente.setNit_rut(dto.nit_rut());
        proveedorExistente.setNombre(dto.nombre());
        proveedorExistente.setTelefono(dto.telefono());
        proveedorExistente.setEmail(dto.email());
        proveedorExistente.setDireccion(dto.direccion());

        return proveedoresRepository.save(proveedorExistente);
    }

    public Proveedores actualizarProveedorParcial(Long id, ProveedorRequestDto dto) {
        Proveedores proveedorExistente = proveedoresRepository.findById(id)
                     .orElseThrow(() -> new InvalidDataException("El proveedor no existe"));

        ProveedorValidate.validate(dto);

        if (!Objects.equals(proveedorExistente.getNit_rut(), dto.nit_rut()) || dto.nit_rut() != null) {
            proveedorExistente.setNit_rut(dto.nit_rut());
        }

        if (!Objects.equals(proveedorExistente.getNombre(), dto.nombre()) || dto.nombre() != null) {
            proveedorExistente.setNombre(dto.nombre());
        }

        if (!Objects.equals(proveedorExistente.getTelefono(), dto.telefono()) || dto.telefono() != null) {
            proveedorExistente.setTelefono(dto.telefono());
        }

        if (!Objects.equals(proveedorExistente.getEmail(), dto.email()) || dto.email() != null) {
            proveedorExistente.setEmail(dto.email());
        }

        return proveedoresRepository.save(proveedorExistente);
    }

    public void eliminarProveedor(Long id) {
        Proveedores proveedorExistente = proveedoresRepository.findById(id)
                     .orElseThrow(() -> new InvalidDataException("El proveedor no existe"));
        proveedoresRepository.delete(proveedorExistente);
    }

}
