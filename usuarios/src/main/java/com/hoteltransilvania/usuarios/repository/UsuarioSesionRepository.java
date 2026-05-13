package com.hoteltransilvania.usuarios.repository;

import com.hoteltransilvania.usuarios.models.UsuarioSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioSesionRepository
        extends JpaRepository<UsuarioSesion, Long> {

    // Buscar sesiones activas
    List<UsuarioSesion> findByActivoTrue();
}