package com.tracker.sgi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracker.sgi.entities.Rol;
import com.tracker.sgi.util.enums.RolEnum;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    Rol findByRol(RolEnum admin);

}
