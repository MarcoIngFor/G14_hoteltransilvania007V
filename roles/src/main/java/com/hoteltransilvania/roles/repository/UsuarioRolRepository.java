package com.hoteltransilvania.roles.repository;

import com.hoteltransilvania.roles.models.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public interface UsuarioRolRepository
        extends JpaRepository<UsuarioRol, Long> {
    
    List<UsuarioRol> findByUsuarioId(Long usuarioId);
}
