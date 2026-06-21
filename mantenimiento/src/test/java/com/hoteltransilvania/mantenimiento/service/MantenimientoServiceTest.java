package com.hoteltransilvania.mantenimiento.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.hoteltransilvania.mantenimiento.client.HabitacionClient;
import com.hoteltransilvania.mantenimiento.dto.EstadoHabitacionDTO;
import com.hoteltransilvania.mantenimiento.dto.MantenimientoDTO;
import com.hoteltransilvania.mantenimiento.exception.ResourceNotFoundException;
import com.hoteltransilvania.mantenimiento.model.MantenimientoModel;
import com.hoteltransilvania.mantenimiento.repository.MantenimientoRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class MantenimientoServiceTest {

    @Mock
    private MantenimientoRepository mantenimientoRepository;

    @Mock
    private HabitacionClient habitacionClient;

    @InjectMocks
    private MantenimientoService mantenimientoService;

    @Test
    @DisplayName("Debe guardar mantenimiento correctamente")
    void shouldSaveMantenimientoSuccessfully() {

        MantenimientoDTO dto = crearDTO("PENDIENTE");

        MantenimientoModel guardado = crearModel(1L, "PENDIENTE");

        when(mantenimientoRepository.save(any(MantenimientoModel.class)))
                .thenReturn(guardado);

        MantenimientoModel resultado = mantenimientoService.guardar(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("PENDIENTE", resultado.getEstado());

        verify(mantenimientoRepository).save(any(MantenimientoModel.class));
        verifyNoInteractions(habitacionClient);
    }

    @Test
    @DisplayName("Debe guardar mantenimiento EN_PROCESO y marcar habitación no disponible")
    void shouldSaveEnProcesoAndSetHabitacionUnavailable() {

        MantenimientoDTO dto = crearDTO("EN_PROCESO");

        MantenimientoModel guardado = crearModel(1L, "EN_PROCESO");

        when(mantenimientoRepository.save(any(MantenimientoModel.class)))
                .thenReturn(guardado);

        MantenimientoModel resultado = mantenimientoService.guardar(dto);

        assertNotNull(resultado);
        assertEquals("EN_PROCESO", resultado.getEstado());

        verify(habitacionClient)
                .actualizarEstadoHabitacion(eq(10L), any(EstadoHabitacionDTO.class));

        verify(mantenimientoRepository).save(any(MantenimientoModel.class));
    }

    @Test
    @DisplayName("Debe guardar mantenimiento FINALIZADO y marcar habitación disponible")
    void shouldSaveFinalizadoAndSetHabitacionAvailable() {

        MantenimientoDTO dto = crearDTO("FINALIZADO");

        MantenimientoModel guardado = crearModel(1L, "FINALIZADO");

        when(mantenimientoRepository.save(any(MantenimientoModel.class)))
                .thenReturn(guardado);

        MantenimientoModel resultado = mantenimientoService.guardar(dto);

        assertNotNull(resultado);
        assertEquals("FINALIZADO", resultado.getEstado());

        verify(habitacionClient)
                .actualizarEstadoHabitacion(eq(10L), any(EstadoHabitacionDTO.class));

        verify(mantenimientoRepository).save(any(MantenimientoModel.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si fecha inicio es anterior a hoy")
    void shouldThrowExceptionWhenFechaInicioIsBeforeToday() {

        MantenimientoDTO dto = crearDTO("PENDIENTE");
        dto.setFechaInicio(LocalDate.now().minusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(1));

        assertThrows(
                ResponseStatusException.class,
                () -> mantenimientoService.guardar(dto)
        );

        verify(mantenimientoRepository, never()).save(any(MantenimientoModel.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si fecha fin no es posterior a fecha inicio")
    void shouldThrowExceptionWhenFechaFinIsNotAfterFechaInicio() {

        MantenimientoDTO dto = crearDTO("PENDIENTE");
        dto.setFechaInicio(LocalDate.now().plusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(1));

        assertThrows(
                ResponseStatusException.class,
                () -> mantenimientoService.guardar(dto)
        );

        verify(mantenimientoRepository, never()).save(any(MantenimientoModel.class));
    }

    @Test
    @DisplayName("Debe listar todos los mantenimientos")
    void shouldListAllMantenimientos() {

        MantenimientoModel m1 = crearModel(1L, "PENDIENTE");
        MantenimientoModel m2 = crearModel(2L, "FINALIZADO");

        when(mantenimientoRepository.findAll())
                .thenReturn(List.of(m1, m2));

        List<MantenimientoModel> resultado = mantenimientoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(mantenimientoRepository).findAll();
    }

    @Test
    @DisplayName("Debe retornar mantenimiento cuando el ID existe")
    void shouldReturnMantenimientoWhenIdExists() {

        MantenimientoModel mantenimiento = crearModel(1L, "PENDIENTE");

        when(mantenimientoRepository.findById(1L))
                .thenReturn(Optional.of(mantenimiento));

        MantenimientoModel resultado = mantenimientoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("PENDIENTE", resultado.getEstado());

        verify(mantenimientoRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando mantenimiento no existe")
    void shouldThrowExceptionWhenMantenimientoDoesNotExist() {

        when(mantenimientoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> mantenimientoService.buscarPorId(99L)
        );

        verify(mantenimientoRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe eliminar mantenimiento cuando existe")
    void shouldDeleteMantenimientoWhenIdExists() {

        MantenimientoModel mantenimiento = crearModel(1L, "PENDIENTE");

        when(mantenimientoRepository.findById(1L))
                .thenReturn(Optional.of(mantenimiento));

        mantenimientoService.eliminar(1L);

        verify(mantenimientoRepository).findById(1L);
        verify(mantenimientoRepository).delete(mantenimiento);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar mantenimiento inexistente")
    void shouldThrowExceptionWhenDeletingMantenimientoDoesNotExist() {

        when(mantenimientoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> mantenimientoService.eliminar(99L)
        );

        verify(mantenimientoRepository).findById(99L);
        verify(mantenimientoRepository, never()).delete(any(MantenimientoModel.class));
    }

    @Test
    @DisplayName("Debe modificar estado a EN_PROCESO")
    void shouldUpdateEstadoToEnProceso() {

        MantenimientoModel mantenimiento = crearModel(1L, "PENDIENTE");

        when(mantenimientoRepository.findById(1L))
                .thenReturn(Optional.of(mantenimiento));

        when(mantenimientoRepository.save(mantenimiento))
                .thenReturn(mantenimiento);

        MantenimientoModel resultado =
                mantenimientoService.modificarEstado(1L, "EN_PROCESO");

        assertEquals("EN_PROCESO", resultado.getEstado());

        verify(habitacionClient)
                .actualizarEstadoHabitacion(eq(10L), any(EstadoHabitacionDTO.class));

        verify(mantenimientoRepository).save(mantenimiento);
    }

    @Test
    @DisplayName("Debe modificar estado a FINALIZADO")
    void shouldUpdateEstadoToFinalizado() {

        MantenimientoModel mantenimiento = crearModel(1L, "EN_PROCESO");

        when(mantenimientoRepository.findById(1L))
                .thenReturn(Optional.of(mantenimiento));

        when(mantenimientoRepository.save(mantenimiento))
                .thenReturn(mantenimiento);

        MantenimientoModel resultado =
                mantenimientoService.modificarEstado(1L, "FINALIZADO");

        assertEquals("FINALIZADO", resultado.getEstado());

        verify(habitacionClient)
                .actualizarEstadoHabitacion(eq(10L), any(EstadoHabitacionDTO.class));

        verify(mantenimientoRepository).save(mantenimiento);
    }

    @Test
    @DisplayName("Debe lanzar excepción con estado inválido")
    void shouldThrowExceptionWhenEstadoIsInvalid() {

        MantenimientoModel mantenimiento = crearModel(1L, "PENDIENTE");

        when(mantenimientoRepository.findById(1L))
                .thenReturn(Optional.of(mantenimiento));

        assertThrows(
                ResponseStatusException.class,
                () -> mantenimientoService.modificarEstado(1L, "CANCELADO")
        );

        verify(mantenimientoRepository).findById(1L);
        verify(mantenimientoRepository, never()).save(any(MantenimientoModel.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al modificar estado de mantenimiento inexistente")
    void shouldThrowExceptionWhenUpdatingEstadoMantenimientoDoesNotExist() {

        when(mantenimientoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> mantenimientoService.modificarEstado(99L, "FINALIZADO")
        );

        verify(mantenimientoRepository).findById(99L);
        verify(mantenimientoRepository, never()).save(any(MantenimientoModel.class));
    }

    @Test
    @DisplayName("Debe actualizar mantenimiento cuando existe")
    void shouldUpdateMantenimientoWhenIdExists() {

        MantenimientoModel existente = crearModel(1L, "PENDIENTE");

        MantenimientoDTO dto = crearDTO("FINALIZADO");
        dto.setDescripcion("Reparación finalizada");

        when(mantenimientoRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(mantenimientoRepository.save(existente))
                .thenReturn(existente);

        MantenimientoModel resultado =
                mantenimientoService.actualizar(1L, dto);

        assertNotNull(resultado);
        assertEquals("Reparación finalizada", resultado.getDescripcion());
        assertEquals("FINALIZADO", resultado.getEstado());

        verify(mantenimientoRepository).findById(1L);
        verify(mantenimientoRepository).save(existente);
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar mantenimiento inexistente")
    void shouldThrowExceptionWhenUpdatingMantenimientoDoesNotExist() {

        MantenimientoDTO dto = crearDTO("PENDIENTE");

        when(mantenimientoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> mantenimientoService.actualizar(99L, dto)
        );

        verify(mantenimientoRepository).findById(99L);
        verify(mantenimientoRepository, never()).save(any(MantenimientoModel.class));
    }

    private MantenimientoDTO crearDTO(String estado) {

        MantenimientoDTO dto = new MantenimientoDTO();
        dto.setIdHabitacion(10L);
        dto.setDescripcion("Reparación de aire acondicionado");
        dto.setFechaInicio(LocalDate.now().plusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(3));
        dto.setEstado(estado);

        return dto;
    }

    private MantenimientoModel crearModel(Long id, String estado) {

        MantenimientoModel mantenimiento = new MantenimientoModel();
        mantenimiento.setId(id);
        mantenimiento.setIdHabitacion(10L);
        mantenimiento.setDescripcion("Reparación de aire acondicionado");
        mantenimiento.setFechaInicio(LocalDate.now().plusDays(1));
        mantenimiento.setFechaFin(LocalDate.now().plusDays(3));
        mantenimiento.setEstado(estado);

        return mantenimiento;
    }
}