package com.hoteltransilvania.servicioextra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoteltransilvania.servicioextra.Model.TipoServicioModel;

@Repository
public interface TipoServicioRepository extends JpaRepository<TipoServicioModel, Long>{

}
