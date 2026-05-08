package com.hoteltransilvania.reservas.repository;

import com.hoteltransilvania.reservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}