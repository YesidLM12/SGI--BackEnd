package com.tracker.sgi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracker.sgi.entities.Ordenes;

@Repository
public interface OrdenRepository extends JpaRepository<Ordenes, Long> {

}
