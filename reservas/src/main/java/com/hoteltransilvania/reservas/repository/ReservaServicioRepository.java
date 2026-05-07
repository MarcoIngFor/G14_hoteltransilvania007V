package com.hoteltransilvania.reservas.repository;

import com.hoteltransilvania.reservas.entity.ReservaServicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaServicioRepository extends JpaRepository<ReservaServicio, Long> {

    // Obtener todos los servicios de una reserva
    List<ReservaServicio> findByReservaId(Long reservaId);
}