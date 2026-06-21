package com.hoteltransilvania.consumo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.hoteltransilvania.consumo.client.ClienteClient;
import com.hoteltransilvania.consumo.client.HabitacionClient;
import com.hoteltransilvania.consumo.client.ReservaClient;
import com.hoteltransilvania.consumo.client.ServicioExtraClient;
import com.hoteltransilvania.consumo.dto.ConsumoDTO;
import com.hoteltransilvania.consumo.dto.ServicioExtraDTO;
import com.hoteltransilvania.consumo.dto.RespuestaDTO.ConsumoRespuestaDTO;
import com.hoteltransilvania.consumo.exception.ResourceNotFoundException;
import com.hoteltransilvania.consumo.model.ConsumoModel;
import com.hoteltransilvania.consumo.repository.ConsumoRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConsumoServiceTest {

    @Mock
    private ConsumoRepository consumoRepository;

    @Mock
    private ServicioExtraClient servicioExtraClient;

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private HabitacionClient habitacionClient;

    @Mock
    private ReservaClient reservaClient;

    @InjectMocks
    private ConsumoService consumoService;

    @Test
    @DisplayName("Debe guardar consumo correctamente")
    void shouldSaveConsumoSuccessfully() {

        ConsumoDTO dto = new ConsumoDTO();
        dto.setIdServicioExtra(1L);
        dto.setIdReserva(2L);
        dto.setIdCliente(3L);
        dto.setIdHabitacion(4L);
        dto.setCantidad(2);

        ServicioExtraDTO servicioExtra = new ServicioExtraDTO();
        servicioExtra.setId(1L);
        servicioExtra.setNombre("Desayuno buffet");
        servicioExtra.setPrecio(8500.0);

        ConsumoModel consumoGuardado = new ConsumoModel();
        consumoGuardado.setId(1L);
        consumoGuardado.setIdServicioExtra(1L);
        consumoGuardado.setIdReserva(2L);
        consumoGuardado.setIdCliente(3L);
        consumoGuardado.setIdHabitacion(4L);
        consumoGuardado.setCantidad(2);
        consumoGuardado.setTotalConsumo(17000.0);

        when(servicioExtraClient.buscarPorId(1L))
                .thenReturn(servicioExtra);

        when(consumoRepository.save(any(ConsumoModel.class)))
                .thenReturn(consumoGuardado);

        ConsumoRespuestaDTO respuesta = consumoService.guardar(dto);

        assertNotNull(respuesta);
        assertEquals("Consumo registrado correctamente", respuesta.getMessage());
        assertEquals(1L, respuesta.getIdServicioExtra());
        assertEquals(3L, respuesta.getIdCliente());
        assertEquals(2L, respuesta.getIdReserva());

        verify(clienteClient).buscarPorId(3L);
        verify(habitacionClient).buscarPorId(4L);
        verify(reservaClient).buscarPorId(2L);
        verify(servicioExtraClient).buscarPorId(1L);
        verify(consumoRepository).save(any(ConsumoModel.class));
    }

    @Test
    @DisplayName("Debe listar todos los consumos")
    void shouldListAllConsumos() {

        ConsumoModel consumo1 = new ConsumoModel();
        consumo1.setId(1L);
        consumo1.setIdCliente(3L);

        ConsumoModel consumo2 = new ConsumoModel();
        consumo2.setId(2L);
        consumo2.setIdCliente(4L);

        when(consumoRepository.findAll())
                .thenReturn(List.of(consumo1, consumo2));

        List<ConsumoModel> resultado = consumoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());

        verify(consumoRepository).findAll();
    }

    @Test
    @DisplayName("Debe retornar consumo cuando el ID existe")
    void shouldReturnConsumoWhenIdExists() {

        ConsumoModel consumo = new ConsumoModel();
        consumo.setId(1L);
        consumo.setIdCliente(3L);
        consumo.setIdReserva(2L);
        consumo.setIdHabitacion(4L);
        consumo.setIdServicioExtra(1L);
        consumo.setCantidad(2);
        consumo.setTotalConsumo(17000.0);

        when(consumoRepository.findById(1L))
                .thenReturn(Optional.of(consumo));

        ConsumoModel resultado = consumoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(3L, resultado.getIdCliente());
        assertEquals(2L, resultado.getIdReserva());
        assertEquals(4L, resultado.getIdHabitacion());
        assertEquals(17000.0, resultado.getTotalConsumo());

        verify(consumoRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el consumo no existe")
    void shouldThrowExceptionWhenConsumoDoesNotExist() {

        when(consumoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> consumoService.buscarPorId(99L)
        );

        verify(consumoRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe buscar consumos por cliente")
    void shouldFindConsumosByCliente() {

        ConsumoModel consumo = new ConsumoModel();
        consumo.setId(1L);
        consumo.setIdCliente(3L);

        when(consumoRepository.findByIdCliente(3L))
                .thenReturn(List.of(consumo));

        List<ConsumoModel> resultado = consumoService.buscarPorCliente(3L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(3L, resultado.get(0).getIdCliente());

        verify(consumoRepository).findByIdCliente(3L);
    }

    @Test
    @DisplayName("Debe buscar consumos por reserva")
    void shouldFindConsumosByReserva() {

        ConsumoModel consumo = new ConsumoModel();
        consumo.setId(1L);
        consumo.setIdReserva(2L);

        when(consumoRepository.findByIdReserva(2L))
                .thenReturn(List.of(consumo));

        List<ConsumoModel> resultado = consumoService.buscarPorReserva(2L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(2L, resultado.get(0).getIdReserva());

        verify(consumoRepository).findByIdReserva(2L);
    }

    @Test
    @DisplayName("Debe buscar consumos por habitación")
    void shouldFindConsumosByHabitacion() {

        ConsumoModel consumo = new ConsumoModel();
        consumo.setId(1L);
        consumo.setIdHabitacion(4L);

        when(consumoRepository.findByIdHabitacion(4L))
                .thenReturn(List.of(consumo));

        List<ConsumoModel> resultado = consumoService.buscarPorHabitacion(4L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(4L, resultado.get(0).getIdHabitacion());

        verify(consumoRepository).findByIdHabitacion(4L);
    }

    @Test
    @DisplayName("Debe eliminar consumo cuando el ID existe")
    void shouldDeleteConsumoWhenIdExists() {

        ConsumoModel consumo = new ConsumoModel();
        consumo.setId(1L);

        when(consumoRepository.findById(1L))
                .thenReturn(Optional.of(consumo));

        consumoService.eliminar(1L);

        verify(consumoRepository).findById(1L);
        verify(consumoRepository).delete(consumo);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar consumo inexistente")
    void shouldThrowExceptionWhenDeletingConsumoDoesNotExist() {

        when(consumoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> consumoService.eliminar(99L)
        );

        verify(consumoRepository).findById(99L);
        verify(consumoRepository, never()).delete(any(ConsumoModel.class));
    }

    @Test
    @DisplayName("Debe actualizar consumo correctamente")
    void shouldUpdateConsumoWhenIdExists() {

        ConsumoModel consumoExistente = new ConsumoModel();
        consumoExistente.setId(1L);
        consumoExistente.setCantidad(1);

        ConsumoDTO dto = new ConsumoDTO();
        dto.setIdServicioExtra(1L);
        dto.setIdReserva(2L);
        dto.setIdCliente(3L);
        dto.setIdHabitacion(4L);
        dto.setCantidad(3);

        ServicioExtraDTO servicioExtra = new ServicioExtraDTO();
        servicioExtra.setId(1L);
        servicioExtra.setNombre("Desayuno buffet");
        servicioExtra.setPrecio(8500.0);

        ConsumoModel consumoActualizado = new ConsumoModel();
        consumoActualizado.setId(1L);
        consumoActualizado.setIdServicioExtra(1L);
        consumoActualizado.setIdReserva(2L);
        consumoActualizado.setIdCliente(3L);
        consumoActualizado.setIdHabitacion(4L);
        consumoActualizado.setCantidad(3);
        consumoActualizado.setTotalConsumo(25500.0);

        when(consumoRepository.findById(1L))
                .thenReturn(Optional.of(consumoExistente));

        when(servicioExtraClient.buscarPorId(1L))
                .thenReturn(servicioExtra);

        when(consumoRepository.save(consumoExistente))
                .thenReturn(consumoActualizado);

        ConsumoModel resultado = consumoService.actualizar(1L, dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(3L, resultado.getIdCliente());
        assertEquals(2L, resultado.getIdReserva());
        assertEquals(4L, resultado.getIdHabitacion());
        assertEquals(3, resultado.getCantidad());
        assertEquals(25500.0, resultado.getTotalConsumo());

        verify(consumoRepository).findById(1L);
        verify(clienteClient).buscarPorId(3L);
        verify(habitacionClient).buscarPorId(4L);
        verify(reservaClient).buscarPorId(2L);
        verify(servicioExtraClient).buscarPorId(1L);
        verify(consumoRepository).save(consumoExistente);
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar consumo inexistente")
    void shouldThrowExceptionWhenUpdatingConsumoDoesNotExist() {

        ConsumoDTO dto = new ConsumoDTO();
        dto.setIdServicioExtra(1L);
        dto.setIdReserva(2L);
        dto.setIdCliente(3L);
        dto.setIdHabitacion(4L);
        dto.setCantidad(3);

        when(consumoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> consumoService.actualizar(99L, dto)
        );

        verify(consumoRepository).findById(99L);
        verify(consumoRepository, never()).save(any(ConsumoModel.class));
        verify(servicioExtraClient, never()).buscarPorId(anyLong());
    }
}