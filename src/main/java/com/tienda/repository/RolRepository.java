package com.tienda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tienda.domain.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    public Optional<Rol> findByRol(String rol);

}
