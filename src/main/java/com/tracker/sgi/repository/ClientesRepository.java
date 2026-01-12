package com.tracker.sgi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracker.sgi.entities.Clientes;

import java.util.Optional;

@Repository
public interface ClientesRepository extends JpaRepository<Clientes, Long> {

    Optional<Clientes> findByEmail(String email);

}
