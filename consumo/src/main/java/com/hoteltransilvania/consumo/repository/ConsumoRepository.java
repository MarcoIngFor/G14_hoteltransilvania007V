package com.hoteltransilvania.consumo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoteltransilvania.consumo.model.ConsumoModel;

@Repository
public interface ConsumoRepository extends JpaRepository<ConsumoModel, Long> {

    List<ConsumoModel> findByIdCliente(Long idCliente);

    List<ConsumoModel> findByIdReserva(Long idReserva);

    List<ConsumoModel> findByIdHabitacion(Long idHabitacion);
}
