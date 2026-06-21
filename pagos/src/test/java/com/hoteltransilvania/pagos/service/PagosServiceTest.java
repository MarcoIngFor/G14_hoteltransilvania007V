package com.hoteltransilvania.pagos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.hoteltransilvania.pagos.client.ConsumoClient;
import com.hoteltransilvania.pagos.client.ReservaClient;
import com.hoteltransilvania.pagos.dto.ConsumoDTO;
import com.hoteltransilvania.pagos.dto.PagosDTO;
import com.hoteltransilvania.pagos.dto.ReservaDTO;
import com.hoteltransilvania.pagos.exception.ResourceNotFoundException;
import com.hoteltransilvania.pagos.model.PagosModel;
import com.hoteltransilvania.pagos.repository.PagosRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PagosServiceTest {

    @Mock
    private PagosRepository pagosRepository;

    @Mock
    private ReservaClient reservaClient;

    @Mock
    private ConsumoClient consumoClient;

    @InjectMocks
    private PagosService pagosService;

    @Test
    @DisplayName("Debe listar todos los pagos")
    void shouldListAllPagos() {

        PagosModel pago1 = crearPago();
        PagosModel pago2 = crearPago();
        pago2.setId(2L);

        when(pagosRepository.findAll())
                .thenReturn(List.of(pago1, pago2));

        List<PagosModel> resultado =
                pagosService.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(pagosRepository).findAll();
    }

    @Test
    @DisplayName("Debe retornar pago cuando el ID existe")
    void shouldReturnPagoWhenIdExists() {

        PagosModel pago = crearPago();

        when(pagosRepository.findById(1L))
                .thenReturn(Optional.of(pago));

        PagosModel resultado =
                pagosService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getIdReserva());

        verify(pagosRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando pago no existe")
    void shouldThrowExceptionWhenPagoDoesNotExist() {

        when(pagosRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> pagosService.buscarPorId(99L)
        );

        verify(pagosRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe retornar pago por reserva cuando existe")
    void shouldReturnPagoByReservaWhenExists() {

        PagosModel pago = crearPago();

        when(pagosRepository.findByIdReserva(1L))
                .thenReturn(Optional.of(pago));

        PagosModel resultado =
                pagosService.buscarPorReserva(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdReserva());

        verify(pagosRepository).findByIdReserva(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando no existe pago por reserva")
    void shouldThrowExceptionWhenPagoByReservaDoesNotExist() {

        when(pagosRepository.findByIdReserva(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> pagosService.buscarPorReserva(99L)
        );

        verify(pagosRepository).findByIdReserva(99L);
    }

    @Test
    @DisplayName("Debe registrar pago correctamente")
    void shouldRegisterPagoSuccessfully() {

        PagosDTO dto = crearDTO();

        ReservaDTO reserva = crearReserva();
        List<ConsumoDTO> consumos = crearConsumos();

        PagosModel pagoGuardado = crearPago();
        pagoGuardado.setSubtotalHabitacion(200000.0);
        pagoGuardado.setSubtotalServicios(30000.0);
        pagoGuardado.setTotalPagar(230000.0);

        when(pagosRepository.findByIdReserva(1L))
                .thenReturn(Optional.empty());

        when(reservaClient.obtenerReserva(1L))
                .thenReturn(reserva);

        when(consumoClient.obtenerConsumosReserva(1L))
                .thenReturn(consumos);

        when(pagosRepository.save(any(PagosModel.class)))
                .thenReturn(pagoGuardado);

        PagosModel resultado =
                pagosService.registrar(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdReserva());
        assertEquals(200000.0, resultado.getSubtotalHabitacion());
        assertEquals(30000.0, resultado.getSubtotalServicios());
        assertEquals(230000.0, resultado.getTotalPagar());
        assertEquals("TARJETA", resultado.getMetodoPago());
        assertEquals("PAGADO", resultado.getEstadoPago());

        verify(pagosRepository).findByIdReserva(1L);
        verify(reservaClient).obtenerReserva(1L);
        verify(consumoClient).obtenerConsumosReserva(1L);
        verify(pagosRepository).save(any(PagosModel.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si ya existe pago para la reserva")
    void shouldThrowExceptionWhenPagoAlreadyExistsForReserva() {

        PagosDTO dto = crearDTO();
        PagosModel pagoExistente = crearPago();

        when(pagosRepository.findByIdReserva(1L))
                .thenReturn(Optional.of(pagoExistente));

        assertThrows(
                ResourceNotFoundException.class,
                () -> pagosService.registrar(dto)
        );

        verify(pagosRepository).findByIdReserva(1L);
        verifyNoInteractions(reservaClient, consumoClient);
        verify(pagosRepository, never()).save(any(PagosModel.class));
    }

    @Test
    @DisplayName("Debe actualizar pago correctamente")
    void shouldUpdatePagoSuccessfully() {

        PagosModel pagoExistente = crearPago();

        PagosDTO dto = new PagosDTO();
        dto.setIdReserva(2L);
        dto.setMetodoPago("EFECTIVO");

        when(pagosRepository.findById(1L))
                .thenReturn(Optional.of(pagoExistente));

        when(pagosRepository.save(pagoExistente))
                .thenReturn(pagoExistente);

        PagosModel resultado =
                pagosService.actualizar(1L, dto);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getIdReserva());
        assertEquals("EFECTIVO", resultado.getMetodoPago());

        verify(pagosRepository).findById(1L);
        verify(pagosRepository).save(pagoExistente);
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar pago inexistente")
    void shouldThrowExceptionWhenUpdatingPagoDoesNotExist() {

        PagosDTO dto = crearDTO();

        when(pagosRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> pagosService.actualizar(99L, dto)
        );

        verify(pagosRepository).findById(99L);
        verify(pagosRepository, never()).save(any(PagosModel.class));
    }

    @Test
    @DisplayName("Debe eliminar pago cuando existe")
    void shouldDeletePagoWhenIdExists() {

        PagosModel pago = crearPago();

        when(pagosRepository.findById(1L))
                .thenReturn(Optional.of(pago));

        pagosService.eliminar(1L);

        verify(pagosRepository).findById(1L);
        verify(pagosRepository).delete(pago);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar pago inexistente")
    void shouldThrowExceptionWhenDeletingPagoDoesNotExist() {

        when(pagosRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> pagosService.eliminar(99L)
        );

        verify(pagosRepository).findById(99L);
        verify(pagosRepository, never()).delete(any(PagosModel.class));
    }

    private PagosDTO crearDTO() {

        PagosDTO dto = new PagosDTO();
        dto.setIdReserva(1L);
        dto.setMetodoPago("TARJETA");

        return dto;
    }

    private PagosModel crearPago() {

        PagosModel pago = new PagosModel();
        pago.setId(1L);
        pago.setIdReserva(1L);
        pago.setSubtotalHabitacion(200000.0);
        pago.setSubtotalServicios(30000.0);
        pago.setTotalPagar(230000.0);
        pago.setMetodoPago("TARJETA");
        pago.setEstadoPago("PAGADO");

        return pago;
    }

    private ReservaDTO crearReserva() {

        ReservaDTO reserva = new ReservaDTO();
        reserva.setId(1L);
        reserva.setIdCliente(1L);
        reserva.setIdHabitacion(1L);
        reserva.setMontoTotal(200000.0);

        return reserva;
    }

    private List<ConsumoDTO> crearConsumos() {

        ConsumoDTO consumo1 = new ConsumoDTO();
        consumo1.setId(1L);
        consumo1.setIdReserva(1L);
        consumo1.setTotalConsumo(10000.0);

        ConsumoDTO consumo2 = new ConsumoDTO();
        consumo2.setId(2L);
        consumo2.setIdReserva(1L);
        consumo2.setTotalConsumo(20000.0);

        return List.of(consumo1, consumo2);
    }
}