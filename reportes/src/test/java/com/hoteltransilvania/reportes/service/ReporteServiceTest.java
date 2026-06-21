package com.hoteltransilvania.reportes.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.hoteltransilvania.reportes.dto.ReporteDTO;
import com.hoteltransilvania.reportes.exception.ResourceNotFoundException;
import com.hoteltransilvania.reportes.model.ReporteModel;
import com.hoteltransilvania.reportes.repository.ReporteRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ReporteService reporteService;

    @Test
    @DisplayName("Debe guardar reporte correctamente")
    void shouldSaveReporteSuccessfully() {

        ReporteDTO dto = crearDTO();
        ReporteModel guardado = crearReporte();

        when(reporteRepository.save(any(ReporteModel.class)))
                .thenReturn(guardado);

        ReporteModel resultado = reporteService.guardar(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("RESERVAS", resultado.getTipo());
        assertEquals("Reporte mensual de reservas e ingresos", resultado.getDescripcion());
        assertEquals(25, resultado.getTotalReservas());
        assertEquals(1250000.0, resultado.getTotalIngresos());

        verify(reporteRepository).save(any(ReporteModel.class));
    }

    @Test
    @DisplayName("Debe listar todos los reportes")
    void shouldListAllReportes() {

        ReporteModel reporte1 = crearReporte();
        ReporteModel reporte2 = crearReporte();
        reporte2.setId(2L);

        when(reporteRepository.findAll())
                .thenReturn(List.of(reporte1, reporte2));

        List<ReporteModel> resultado = reporteService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(reporteRepository).findAll();
    }

    @Test
    @DisplayName("Debe retornar reporte cuando el ID existe")
    void shouldReturnReporteWhenIdExists() {

        ReporteModel reporte = crearReporte();

        when(reporteRepository.findById(1L))
                .thenReturn(Optional.of(reporte));

        ReporteModel resultado = reporteService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("RESERVAS", resultado.getTipo());

        verify(reporteRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando reporte no existe")
    void shouldThrowExceptionWhenReporteDoesNotExist() {

        when(reporteRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reporteService.buscarPorId(99L)
        );

        verify(reporteRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe buscar reportes por tipo")
    void shouldFindReportesByTipo() {

        ReporteModel reporte = crearReporte();

        when(reporteRepository.findByTipo("RESERVAS"))
                .thenReturn(List.of(reporte));

        List<ReporteModel> resultado = reporteService.buscarPorTipo("RESERVAS");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("RESERVAS", resultado.get(0).getTipo());

        verify(reporteRepository).findByTipo("RESERVAS");
    }

    @Test
    @DisplayName("Debe actualizar reporte cuando existe")
    void shouldUpdateReporteWhenIdExists() {

        ReporteModel existente = crearReporte();

        ReporteDTO dto = new ReporteDTO();
        dto.setTipo("INGRESOS");
        dto.setDescripcion("Reporte actualizado de ingresos");
        dto.setTotalReservas(30);
        dto.setTotalIngresos(1500000.0);

        when(reporteRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(reporteRepository.save(existente))
                .thenReturn(existente);

        ReporteModel resultado = reporteService.actualizar(1L, dto);

        assertNotNull(resultado);
        assertEquals("INGRESOS", resultado.getTipo());
        assertEquals("Reporte actualizado de ingresos", resultado.getDescripcion());
        assertEquals(30, resultado.getTotalReservas());
        assertEquals(1500000.0, resultado.getTotalIngresos());

        verify(reporteRepository).findById(1L);
        verify(reporteRepository).save(existente);
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar reporte inexistente")
    void shouldThrowExceptionWhenUpdatingReporteDoesNotExist() {

        ReporteDTO dto = crearDTO();

        when(reporteRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reporteService.actualizar(99L, dto)
        );

        verify(reporteRepository).findById(99L);
        verify(reporteRepository, never()).save(any(ReporteModel.class));
    }

    @Test
    @DisplayName("Debe eliminar reporte cuando existe")
    void shouldDeleteReporteWhenIdExists() {

        ReporteModel reporte = crearReporte();

        when(reporteRepository.findById(1L))
                .thenReturn(Optional.of(reporte));

        reporteService.eliminar(1L);

        verify(reporteRepository).findById(1L);
        verify(reporteRepository).delete(reporte);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar reporte inexistente")
    void shouldThrowExceptionWhenDeletingReporteDoesNotExist() {

        when(reporteRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reporteService.eliminar(99L)
        );

        verify(reporteRepository).findById(99L);
        verify(reporteRepository, never()).delete(any(ReporteModel.class));
    }

    private ReporteDTO crearDTO() {

        ReporteDTO dto = new ReporteDTO();
        dto.setTipo("RESERVAS");
        dto.setDescripcion("Reporte mensual de reservas e ingresos");
        dto.setTotalReservas(25);
        dto.setTotalIngresos(1250000.0);

        return dto;
    }

    private ReporteModel crearReporte() {

        ReporteModel reporte = new ReporteModel();
        reporte.setId(1L);
        reporte.setTipo("RESERVAS");
        reporte.setDescripcion("Reporte mensual de reservas e ingresos");
        reporte.setTotalReservas(25);
        reporte.setTotalIngresos(1250000.0);

        return reporte;
    }
}