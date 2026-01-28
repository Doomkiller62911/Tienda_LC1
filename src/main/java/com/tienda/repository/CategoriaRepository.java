package com.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tienda.domain.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    public List<Categoria> findByActivoTrue();
}
