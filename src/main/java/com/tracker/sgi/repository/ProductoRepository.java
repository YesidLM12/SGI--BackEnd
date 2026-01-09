package com.tracker.sgi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracker.sgi.entities.Productos;

@Repository
public interface ProductoRepository extends JpaRepository<Productos, Long> {

}
