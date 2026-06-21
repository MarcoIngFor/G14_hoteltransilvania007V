package com.hoteltransilvania.reportes.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hoteltransilvania.reportes.dto.ReporteDTO;
import com.hoteltransilvania.reportes.exception.ResourceNotFoundException;
import com.hoteltransilvania.reportes.model.ReporteModel;
import com.hoteltransilvania.reportes.repository.ReporteRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;

    public ReporteService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    public ReporteModel guardar(ReporteDTO dto) {
        log.info("Registrando reporte tipo: {}", dto.getTipo());
        ReporteModel reporte = new ReporteModel();
        reporte.setTipo(dto.getTipo());
        reporte.setDescripcion(dto.getDescripcion());
        reporte.setFechaGeneracion(LocalDate.now());
        reporte.setTotalReservas(dto.getTotalReservas());
        reporte.setTotalIngresos(dto.getTotalIngresos());
        ReporteModel guardado = reporteRepository.save(reporte);
        log.info("Reporte registrado correctamente ID: {}", guardado.getId());
        return guardado;
    }

    public List<ReporteModel> listarTodos() {
        log.info("Listando todos los reportes");
        return reporteRepository.findAll();
    }

    public ReporteModel buscarPorId(Long id) {
        log.info("Buscando reporte ID: {}", id);
        return reporteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Reporte no encontrado ID: {}", id);
                    return new ResourceNotFoundException("Reporte no encontrado con ID: " + id);
                });
    }

    public List<ReporteModel> buscarPorTipo(String tipo) {
        log.info("Buscando reportes por tipo: {}", tipo);
        return reporteRepository.findByTipo(tipo);
    }

    public ReporteModel actualizar(Long id, ReporteDTO dto) {
        log.info("Actualizando reporte ID: {}", id);
        ReporteModel reporte = buscarPorId(id);
        reporte.setTipo(dto.getTipo());
        reporte.setDescripcion(dto.getDescripcion());
        reporte.setTotalReservas(dto.getTotalReservas());
        reporte.setTotalIngresos(dto.getTotalIngresos());
        ReporteModel actualizado = reporteRepository.save(reporte);
        log.info("Reporte actualizado correctamente ID: {}", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        log.warn("Eliminando reporte ID: {}", id);
        ReporteModel reporte = buscarPorId(id);
        reporteRepository.delete(reporte);
        log.info("Reporte eliminado correctamente ID: {}", id);
    }
}
