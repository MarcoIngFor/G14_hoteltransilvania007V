package com.hoteltransilvania.privilegios.repository;

import com.hoteltransilvania.privilegios.models.Privilegio;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivilegioRepository extends JpaRepository<Privilegio, Long> {

    Optional<Privilegio> findByNombre(String nombre);
}