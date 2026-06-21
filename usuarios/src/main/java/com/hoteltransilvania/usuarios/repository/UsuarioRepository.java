package com.hoteltransilvania.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoteltransilvania.usuarios.model.UsuarioModel;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long>{

    Optional<UsuarioModel> findByUsername(String username);

    Optional<UsuarioModel> findByCorreo(String correo);

    boolean existsByUsername(String username);

    boolean existsByCorreo(String correo);
}
