package com.hoteltransilvania.habitacionesservice.repository;

import com.hoteltransilvania.habitacionesservice.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
}