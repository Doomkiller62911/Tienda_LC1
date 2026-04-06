package com.tienda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tienda.domain.Usuario;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Optional<Usuario> findByUsernameAndActivoTrue(String username);

    public List<Usuario> findByActivoTrue();

    public Optional<Usuario> findByUsername(String username);

    public Optional<Usuario> findByUsernameOrCorreo(String username, String correo);

    public Optional<Usuario> findByUsernameOrPassword(String username, String password);

    public boolean existsByUsernameOrCorreo(String username, String correo);
}
