package com.hoteltransilvania.reportes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoteltransilvania.reportes.model.ReporteModel;

@Repository
public interface ReporteRepository extends JpaRepository<ReporteModel, Long> {
    List<ReporteModel> findByTipo(String tipo);
}
