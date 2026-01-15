package com.tracker.sgi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracker.sgi.entities.DetallesOrden;

@Repository
public interface DetallesOrdenRepository extends JpaRepository<DetallesOrden, Long> {

}
