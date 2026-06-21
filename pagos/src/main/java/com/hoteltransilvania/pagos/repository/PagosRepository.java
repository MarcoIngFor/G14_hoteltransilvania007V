package com.hoteltransilvania.pagos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hoteltransilvania.pagos.model.PagosModel;

@Repository
public interface PagosRepository extends JpaRepository<PagosModel, Long>{

    Optional<PagosModel> findByIdReserva(Long idReserva);
    
}
