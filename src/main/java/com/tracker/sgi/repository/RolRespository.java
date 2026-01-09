package com.tracker.sgi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracker.sgi.entities.Rol;

@Repository
public interface RolRespository extends JpaRepository<Rol, Long> {

}
