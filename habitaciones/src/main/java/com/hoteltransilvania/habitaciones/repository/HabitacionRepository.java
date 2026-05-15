package com.hoteltransilvania.habitaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoteltransilvania.habitaciones.Model.HabitacionModel;

@Repository
public interface HabitacionRepository extends JpaRepository<HabitacionModel, Long>{

    boolean existsByNumero(Integer numero);
}
