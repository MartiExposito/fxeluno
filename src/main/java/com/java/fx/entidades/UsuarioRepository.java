package com.java.fx.entidades;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findByNombre(String nombre);

    boolean existsByNombreAndContraseña(String nombre, String contraseña);

    List<Usuario> findByContraseña(String contraseña);

    Optional<Usuario> findByNombreAndContraseña(String nombre, String contraseña);
}