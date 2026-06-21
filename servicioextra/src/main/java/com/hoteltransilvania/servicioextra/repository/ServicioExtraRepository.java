package com.hoteltransilvania.servicioextra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoteltransilvania.servicioextra.Model.ServicioExtraModel;

@Repository
public interface ServicioExtraRepository extends JpaRepository<ServicioExtraModel, Long>{

}
