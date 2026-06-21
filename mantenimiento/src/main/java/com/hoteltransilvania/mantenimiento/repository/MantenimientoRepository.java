package com.hoteltransilvania.mantenimiento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoteltransilvania.mantenimiento.model.MantenimientoModel;

@Repository
public interface MantenimientoRepository extends JpaRepository<MantenimientoModel,Long>{

    List<MantenimientoModel> findByIdHabitacion(Long idHabitacion);

    List<MantenimientoModel> findByEstado(String estado);
}
