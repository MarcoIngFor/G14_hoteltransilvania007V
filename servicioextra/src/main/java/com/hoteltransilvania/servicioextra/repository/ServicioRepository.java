package com.hoteltransilvania.servicioextra.repository;

import com.hoteltransilvania.servicioextra.entity.Servicio;
// Repositorio que permite operaciones CRUD sobre la entidad Servicio
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioRepository extends JpaRepository<Servicio, Long> { 
}