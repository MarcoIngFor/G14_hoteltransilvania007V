package com.hoteltransilvania.servicioextra.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.hoteltransilvania.servicioextra.Model.ServicioExtraModel;
import com.hoteltransilvania.servicioextra.Model.TipoServicioModel;
import com.hoteltransilvania.servicioextra.dto.ServicioExtraDTO;
import com.hoteltransilvania.servicioextra.dto.RespuestaDTO.SerExtraDTO;
import com.hoteltransilvania.servicioextra.exception.ResourceNotFoundException;
import com.hoteltransilvania.servicioextra.repository.ServicioExtraRepository;
import com.hoteltransilvania.servicioextra.repository.TipoServicioRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServicioExtraServiceTest {

    @Mock
    private ServicioExtraRepository servicioExtraRepository;

    @Mock
    private TipoServicioRepository tipoServicioRepository;

    @InjectMocks
    private ServicioExtraService servicioExtraService;

    @Test
    @DisplayName("Debe listar todos los servicios extra")
    void shouldListAllServiciosExtra() {

        ServicioExtraModel s1 = crearServicioExtra();
        ServicioExtraModel s2 = crearServicioExtra();
        s2.setId(2L);

        when(servicioExtraRepository.findAll())
                .thenReturn(List.of(s1, s2));

        List<ServicioExtraModel> resultado =
                servicioExtraService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(servicioExtraRepository).findAll();
    }

    @Test
    @DisplayName("Debe retornar servicio extra cuando el ID existe")
    void shouldReturnServicioExtraWhenIdExists() {

        ServicioExtraModel servicio = crearServicioExtra();

        when(servicioExtraRepository.findById(1L))
                .thenReturn(Optional.of(servicio));

        ServicioExtraModel resultado =
                servicioExtraService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Desayuno buffet", resultado.getNombre());

        verify(servicioExtraRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando servicio extra no existe")
    void shouldThrowExceptionWhenServicioExtraDoesNotExist() {

        when(servicioExtraRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> servicioExtraService.obtenerPorId(99L)
        );

        verify(servicioExtraRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe guardar servicio extra correctamente")
    void shouldSaveServicioExtraSuccessfully() {

        ServicioExtraDTO dto = crearDTO();
        TipoServicioModel tipo = crearTipoServicio();
        ServicioExtraModel guardado = crearServicioExtra();

        when(tipoServicioRepository.findById(1L))
                .thenReturn(Optional.of(tipo));

        when(servicioExtraRepository.save(any(ServicioExtraModel.class)))
                .thenReturn(guardado);

        SerExtraDTO respuesta =
                servicioExtraService.guardar(dto);

        assertNotNull(respuesta);
        assertEquals(
                "Servicio: Desayuno buffet, agregado Exitosamente",
                respuesta.getMessage()
        );

        verify(tipoServicioRepository).findById(1L);
        verify(servicioExtraRepository).save(any(ServicioExtraModel.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al guardar si tipo servicio no existe")
    void shouldThrowExceptionWhenTipoServicioDoesNotExistOnSave() {

        ServicioExtraDTO dto = crearDTO();

        when(tipoServicioRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> servicioExtraService.guardar(dto)
        );

        verify(tipoServicioRepository).findById(1L);
        verify(servicioExtraRepository, never()).save(any(ServicioExtraModel.class));
    }

    @Test
    @DisplayName("Debe actualizar servicio extra correctamente")
    void shouldUpdateServicioExtraSuccessfully() {

        ServicioExtraDTO dto = crearDTO();
        dto.setNombre("Spa vampírico");
        dto.setDescripcion("Sesión de spa nocturna");
        dto.setPrecio(25000.0);

        ServicioExtraModel existente = crearServicioExtra();
        TipoServicioModel tipo = crearTipoServicio();

        when(servicioExtraRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(tipoServicioRepository.findById(1L))
                .thenReturn(Optional.of(tipo));

        when(servicioExtraRepository.save(existente))
                .thenReturn(existente);

        ServicioExtraModel resultado =
                servicioExtraService.actualizar(1L, dto);

        assertNotNull(resultado);
        assertEquals("Spa vampírico", resultado.getNombre());
        assertEquals("Sesión de spa nocturna", resultado.getDescripcion());
        assertEquals(25000.0, resultado.getPrecio());

        verify(servicioExtraRepository).findById(1L);
        verify(tipoServicioRepository).findById(1L);
        verify(servicioExtraRepository).save(existente);
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar servicio extra inexistente")
    void shouldThrowExceptionWhenUpdatingServicioExtraDoesNotExist() {

        ServicioExtraDTO dto = crearDTO();

        when(servicioExtraRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> servicioExtraService.actualizar(99L, dto)
        );

        verify(servicioExtraRepository).findById(99L);
        verify(tipoServicioRepository, never()).findById(anyLong());
        verify(servicioExtraRepository, never()).save(any(ServicioExtraModel.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar si tipo servicio no existe")
    void shouldThrowExceptionWhenTipoServicioDoesNotExistOnUpdate() {

        ServicioExtraDTO dto = crearDTO();
        ServicioExtraModel existente = crearServicioExtra();

        when(servicioExtraRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(tipoServicioRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> servicioExtraService.actualizar(1L, dto)
        );

        verify(servicioExtraRepository).findById(1L);
        verify(tipoServicioRepository).findById(1L);
        verify(servicioExtraRepository, never()).save(any(ServicioExtraModel.class));
    }

    @Test
    @DisplayName("Debe eliminar servicio extra cuando existe")
    void shouldDeleteServicioExtraWhenIdExists() {

        ServicioExtraModel servicio = crearServicioExtra();

        when(servicioExtraRepository.findById(1L))
                .thenReturn(Optional.of(servicio));

        servicioExtraService.eliminar(1L);

        verify(servicioExtraRepository).findById(1L);
        verify(servicioExtraRepository).delete(servicio);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar servicio extra inexistente")
    void shouldThrowExceptionWhenDeletingServicioExtraDoesNotExist() {

        when(servicioExtraRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> servicioExtraService.eliminar(99L)
        );

        verify(servicioExtraRepository).findById(99L);
        verify(servicioExtraRepository, never()).delete(any(ServicioExtraModel.class));
    }

    private ServicioExtraDTO crearDTO() {

        ServicioExtraDTO dto = new ServicioExtraDTO();
        dto.setNombre("Desayuno buffet");
        dto.setDescripcion("Desayuno buffet disponible para huéspedes");
        dto.setPrecio(8500.0);
        dto.setIdTipoServicio(1L);

        return dto;
    }

    private ServicioExtraModel crearServicioExtra() {

        ServicioExtraModel servicio = new ServicioExtraModel();
        servicio.setId(1L);
        servicio.setNombre("Desayuno buffet");
        servicio.setDescripcion("Desayuno buffet disponible para huéspedes");
        servicio.setPrecio(8500.0);
        servicio.setTipoServicio(crearTipoServicio());

        return servicio;
    }

    private TipoServicioModel crearTipoServicio() {

        TipoServicioModel tipo = new TipoServicioModel();
        tipo.setId(1L);
        tipo.setNombre("Alimentación");

        return tipo;
    }
}