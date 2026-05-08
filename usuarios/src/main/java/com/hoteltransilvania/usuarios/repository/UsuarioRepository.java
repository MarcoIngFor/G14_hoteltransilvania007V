package com.hoteltransilvania.usuarios.repository;

import com.hoteltransilvania.usuarios.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // valida username repetido
    boolean existsByUsername(String username);

    // buscar usuario para login
    Optional<Usuario> findByUsername(String username);
}