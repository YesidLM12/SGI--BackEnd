package com.tracker.sgi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracker.sgi.entities.Categorias;

@Repository 
public interface CategoriaRepository extends JpaRepository<Categorias, Long> {

    Categorias findByNombre(Categorias categoria);

}
