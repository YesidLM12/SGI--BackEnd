package com.tracker.sgi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracker.sgi.entities.Proveedores;

@Repository
public interface ProveedoresRepository extends JpaRepository<Proveedores, Long> {

}
