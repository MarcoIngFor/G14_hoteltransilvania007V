package com.hoteltransilvania.reservas.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hoteltransilvania.reservas.Model.ReservaModel;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaModel,Long>{

    @Query("SELECT COUNT(r) > 0 FROM ReservaModel r " +
           "WHERE r.idHabitacion = :idHabitacion " +
           "AND (:f_inicio < r.fechaFin AND :f_fin > r.fechaInicio)")
    boolean existeTraslape(@Param("idHabitacion") Long idHabitacion, 
                           @Param("f_inicio") LocalDate f_inicio, 
                           @Param("f_fin") LocalDate f_fin);
}
