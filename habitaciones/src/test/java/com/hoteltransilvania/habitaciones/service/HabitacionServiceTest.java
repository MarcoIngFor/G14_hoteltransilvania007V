package com.hoteltransilvania.habitaciones.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.hoteltransilvania.habitaciones.Model.HabitacionModel;
import com.hoteltransilvania.habitaciones.dto.HabitacionDTO;
import com.hoteltransilvania.habitaciones.dto.RespuestaDTO.HabitacionRespuestaDTO;
import com.hoteltransilvania.habitaciones.exception.DuplicateResourceException;
import com.hoteltransilvania.habitaciones.exception.ResourceNotFoundException;
import com.hoteltransilvania.habitaciones.repository.HabitacionRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HabitacionServiceTest {

    @Mock
    private HabitacionRepository habitacionRepository;

    @InjectMocks
    private HabitacionService habitacionService;

    @Test
    @DisplayName("Debe guardar habitación correctamente")
    void shouldSaveHabitacionSuccessfully() {

        HabitacionDTO dto = new HabitacionDTO();
        dto.setNumero(101);
        dto.setTipo("Suite");
        dto.setPrecioPorNoche(35000.0);
        dto.setDisponible(true);

        HabitacionModel habitacionGuardada = new HabitacionModel();
        habitacionGuardada.setId(1L);
        habitacionGuardada.setNumero(101);
        habitacionGuardada.setTipo("Suite");
        habitacionGuardada.setPrecioPorNoche(35000.0);
        habitacionGuardada.setDisponible(true);

        when(habitacionRepository.existsByNumero(dto.getNumero()))
                .thenReturn(false);

        when(habitacionRepository.save(any(HabitacionModel.class)))
                .thenReturn(habitacionGuardada);

        HabitacionRespuestaDTO respuesta = habitacionService.guardar(dto);

        assertNotNull(respuesta);
        assertEquals(
                "La habitación ha sido generada y guardada con éxito",
                respuesta.getMensaje()
        );

        verify(habitacionRepository).existsByNumero(dto.getNumero());
        verify(habitacionRepository).save(any(HabitacionModel.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el número de habitación ya existe")
    void shouldThrowExceptionWhenNumeroAlreadyExists() {

        HabitacionDTO dto = new HabitacionDTO();
        dto.setNumero(101);

        when(habitacionRepository.existsByNumero(dto.getNumero()))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> habitacionService.guardar(dto)
        );

        verify(habitacionRepository).existsByNumero(dto.getNumero());
        verify(habitacionRepository, never()).save(any(HabitacionModel.class));
    }

    @Test
    @DisplayName("Debe listar todas las habitaciones")
    void shouldListAllHabitaciones() {

        HabitacionModel habitacion1 = new HabitacionModel();
        habitacion1.setId(1L);
        habitacion1.setNumero(101);
        habitacion1.setTipo("Suite");

        HabitacionModel habitacion2 = new HabitacionModel();
        habitacion2.setId(2L);
        habitacion2.setNumero(102);
        habitacion2.setTipo("Doble");

        when(habitacionRepository.findAll())
                .thenReturn(List.of(habitacion1, habitacion2));

        List<HabitacionModel> resultado = habitacionService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(101, resultado.get(0).getNumero());
        assertEquals(102, resultado.get(1).getNumero());

        verify(habitacionRepository).findAll();
    }

    @Test
    @DisplayName("Debe retornar habitación cuando el ID existe")
    void shouldReturnHabitacionWhenIdExists() {

        HabitacionModel habitacion = new HabitacionModel();
        habitacion.setId(1L);
        habitacion.setNumero(101);
        habitacion.setTipo("Suite");
        habitacion.setPrecioPorNoche(35000.0);
        habitacion.setDisponible(true);

        when(habitacionRepository.findById(1L))
                .thenReturn(Optional.of(habitacion));

        HabitacionModel resultado = habitacionService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(101, resultado.getNumero());
        assertEquals("Suite", resultado.getTipo());
        assertEquals(35000.0, resultado.getPrecioPorNoche());
        assertTrue(resultado.isDisponible());

        verify(habitacionRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la habitación no existe")
    void shouldThrowExceptionWhenHabitacionDoesNotExist() {

        when(habitacionRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> habitacionService.buscarPorId(99L)
        );

        verify(habitacionRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe actualizar disponibilidad cuando la habitación existe")
    void shouldUpdateDisponibilidadWhenHabitacionExists() {

        HabitacionModel habitacion = new HabitacionModel();
        habitacion.setId(1L);
        habitacion.setNumero(101);
        habitacion.setDisponible(true);

        when(habitacionRepository.findById(1L))
                .thenReturn(Optional.of(habitacion));

        when(habitacionRepository.save(habitacion))
                .thenReturn(habitacion);

        habitacionService.actualizarDisponibilidad(1L, false);

        assertFalse(habitacion.isDisponible());

        verify(habitacionRepository).findById(1L);
        verify(habitacionRepository).save(habitacion);
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar disponibilidad de habitación inexistente")
    void shouldThrowExceptionWhenUpdatingDisponibilidadHabitacionDoesNotExist() {

        when(habitacionRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> habitacionService.actualizarDisponibilidad(99L, false)
        );

        verify(habitacionRepository).findById(99L);
        verify(habitacionRepository, never()).save(any(HabitacionModel.class));
    }

    @Test
    @DisplayName("Debe actualizar habitación cuando el ID existe")
    void shouldUpdateHabitacionWhenIdExists() {

        HabitacionModel habitacionExistente = new HabitacionModel();
        habitacionExistente.setId(1L);
        habitacionExistente.setNumero(101);
        habitacionExistente.setTipo("Simple");
        habitacionExistente.setPrecioPorNoche(25000.0);
        habitacionExistente.setDisponible(true);

        HabitacionDTO dto = new HabitacionDTO();
        dto.setNumero(202);
        dto.setTipo("Suite");
        dto.setPrecioPorNoche(45000.0);
        dto.setDisponible(false);

        HabitacionModel habitacionActualizada = new HabitacionModel();
        habitacionActualizada.setId(1L);
        habitacionActualizada.setNumero(202);
        habitacionActualizada.setTipo("Suite");
        habitacionActualizada.setPrecioPorNoche(45000.0);
        habitacionActualizada.setDisponible(false);

        when(habitacionRepository.findById(1L))
                .thenReturn(Optional.of(habitacionExistente));

        when(habitacionRepository.save(habitacionExistente))
                .thenReturn(habitacionActualizada);

        HabitacionModel resultado = habitacionService.actualizar(1L, dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(202, resultado.getNumero());
        assertEquals("Suite", resultado.getTipo());
        assertEquals(45000.0, resultado.getPrecioPorNoche());
        assertFalse(resultado.isDisponible());

        verify(habitacionRepository).findById(1L);
        verify(habitacionRepository).save(habitacionExistente);
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar habitación inexistente")
    void shouldThrowExceptionWhenUpdatingHabitacionDoesNotExist() {

        HabitacionDTO dto = new HabitacionDTO();
        dto.setNumero(202);

        when(habitacionRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> habitacionService.actualizar(99L, dto)
        );

        verify(habitacionRepository).findById(99L);
        verify(habitacionRepository, never()).save(any(HabitacionModel.class));
    }

    @Test
    @DisplayName("Debe eliminar habitación cuando el ID existe")
    void shouldDeleteHabitacionWhenIdExists() {

        HabitacionModel habitacion = new HabitacionModel();
        habitacion.setId(1L);
        habitacion.setNumero(101);

        when(habitacionRepository.findById(1L))
                .thenReturn(Optional.of(habitacion));

        habitacionService.eliminar(1L);

        verify(habitacionRepository).findById(1L);
        verify(habitacionRepository).delete(habitacion);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar habitación inexistente")
    void shouldThrowExceptionWhenDeletingHabitacionDoesNotExist() {

        when(habitacionRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> habitacionService.eliminar(99L)
        );

        verify(habitacionRepository).findById(99L);
        verify(habitacionRepository, never()).delete(any(HabitacionModel.class));
    }
}