package com.hoteltransilvania.reservas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.hoteltransilvania.reservas.Model.ReservaModel;
import com.hoteltransilvania.reservas.client.ClienteClient;
import com.hoteltransilvania.reservas.client.HabitacionClient;
import com.hoteltransilvania.reservas.client.NotificacionClient;
import com.hoteltransilvania.reservas.dto.ClienteDTO;
import com.hoteltransilvania.reservas.dto.HabitacionDTO;
import com.hoteltransilvania.reservas.dto.NotificacionDTO;
import com.hoteltransilvania.reservas.dto.ReservaDTO;
import com.hoteltransilvania.reservas.dto.RespuestaDTO.ReservaRespuestaDTO;
import com.hoteltransilvania.reservas.exception.ResourceNotFoundException;
import com.hoteltransilvania.reservas.repository.ReservaRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ReservaServicesTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private HabitacionClient habitacionClient;

    @Mock
    private NotificacionClient notificacionClient;

    @InjectMocks
    private ReservaServices reservaServices;

    @Test
    @DisplayName("Debe guardar reserva correctamente")
    void shouldSaveReservaSuccessfully() {

        ReservaDTO dto = crearDTO();
        ClienteDTO cliente = crearCliente();
        HabitacionDTO habitacion = crearHabitacionDisponible();
        ReservaModel reservaGuardada = crearReservaModel();

        when(clienteClient.obtenerCliente(1L))
                .thenReturn(cliente);

        when(habitacionClient.obtenerHabitacion(1L))
                .thenReturn(habitacion);

        when(reservaRepository.existeTraslape(
                dto.getIdHabitacion(),
                dto.getFechaInicio(),
                dto.getFechaFin()))
                .thenReturn(false);

        when(reservaRepository.save(any(ReservaModel.class)))
                .thenReturn(reservaGuardada);

        ReservaRespuestaDTO respuesta =
                reservaServices.guardar(dto);

        assertNotNull(respuesta);
        assertEquals(
                "La Reserva ha sido Registrada con Éxito!!!",
                respuesta.getMensaje()
        );

        verify(clienteClient).obtenerCliente(1L);
        verify(habitacionClient).obtenerHabitacion(1L);
        verify(reservaRepository).existeTraslape(
                dto.getIdHabitacion(),
                dto.getFechaInicio(),
                dto.getFechaFin()
        );
        verify(reservaRepository).save(any(ReservaModel.class));
        verify(notificacionClient).registrarNotificacion(any(NotificacionDTO.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si fecha inicio es anterior a hoy")
    void shouldThrowExceptionWhenFechaInicioIsBeforeToday() {

        ReservaDTO dto = crearDTO();
        dto.setFechaInicio(LocalDate.now().minusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(2));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> reservaServices.guardar(dto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());

        verify(reservaRepository, never()).save(any(ReservaModel.class));
        verifyNoInteractions(clienteClient, habitacionClient, notificacionClient);
    }

    @Test
    @DisplayName("Debe lanzar excepción si fecha fin es anterior a fecha inicio")
    void shouldThrowExceptionWhenFechaFinIsBeforeFechaInicio() {

        ReservaDTO dto = crearDTO();
        dto.setFechaInicio(LocalDate.now().plusDays(5));
        dto.setFechaFin(LocalDate.now().plusDays(3));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> reservaServices.guardar(dto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());

        verify(reservaRepository, never()).save(any(ReservaModel.class));
        verifyNoInteractions(clienteClient, habitacionClient, notificacionClient);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el cliente no existe al guardar")
    void shouldThrowExceptionWhenClienteDoesNotExistOnSave() {

        ReservaDTO dto = crearDTO();

        when(clienteClient.obtenerCliente(1L))
                .thenThrow(new RuntimeException("Cliente no existe"));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> reservaServices.guardar(dto)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());

        verify(clienteClient).obtenerCliente(1L);
        verifyNoInteractions(habitacionClient, notificacionClient);
        verify(reservaRepository, never()).save(any(ReservaModel.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si la habitación no existe al guardar")
    void shouldThrowExceptionWhenHabitacionDoesNotExistOnSave() {

        ReservaDTO dto = crearDTO();

        when(clienteClient.obtenerCliente(1L))
                .thenReturn(crearCliente());

        when(habitacionClient.obtenerHabitacion(1L))
                .thenThrow(new RuntimeException("Habitación no existe"));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> reservaServices.guardar(dto)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());

        verify(clienteClient).obtenerCliente(1L);
        verify(habitacionClient).obtenerHabitacion(1L);
        verifyNoInteractions(notificacionClient);
        verify(reservaRepository, never()).save(any(ReservaModel.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si la habitación no está disponible")
    void shouldThrowExceptionWhenHabitacionIsNotDisponible() {

        ReservaDTO dto = crearDTO();

        when(clienteClient.obtenerCliente(1L))
                .thenReturn(crearCliente());

        when(habitacionClient.obtenerHabitacion(1L))
                .thenReturn(crearHabitacionNoDisponible());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> reservaServices.guardar(dto)
        );

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());

        verify(clienteClient).obtenerCliente(1L);
        verify(habitacionClient).obtenerHabitacion(1L);
        verify(reservaRepository, never()).save(any(ReservaModel.class));
        verifyNoInteractions(notificacionClient);
    }

    @Test
    @DisplayName("Debe lanzar excepción si existe traslape de reserva")
    void shouldThrowExceptionWhenReservaTraslapeExists() {

        ReservaDTO dto = crearDTO();

        when(clienteClient.obtenerCliente(1L))
                .thenReturn(crearCliente());

        when(habitacionClient.obtenerHabitacion(1L))
                .thenReturn(crearHabitacionDisponible());

        when(reservaRepository.existeTraslape(
                dto.getIdHabitacion(),
                dto.getFechaInicio(),
                dto.getFechaFin()))
                .thenReturn(true);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> reservaServices.guardar(dto)
        );

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());

        verify(reservaRepository, never()).save(any(ReservaModel.class));
        verifyNoInteractions(notificacionClient);
    }

    @Test
    @DisplayName("Debe lanzar excepción si fecha fin es igual a fecha inicio")
    void shouldThrowExceptionWhenFechaFinEqualsFechaInicio() {

        ReservaDTO dto = crearDTO();
        dto.setFechaInicio(LocalDate.now().plusDays(2));
        dto.setFechaFin(LocalDate.now().plusDays(2));

        when(clienteClient.obtenerCliente(1L))
                .thenReturn(crearCliente());

        when(habitacionClient.obtenerHabitacion(1L))
                .thenReturn(crearHabitacionDisponible());

        when(reservaRepository.existeTraslape(
                dto.getIdHabitacion(),
                dto.getFechaInicio(),
                dto.getFechaFin()))
                .thenReturn(false);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> reservaServices.guardar(dto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());

        verify(reservaRepository, never()).save(any(ReservaModel.class));
        verifyNoInteractions(notificacionClient);
    }

    @Test
    @DisplayName("Debe listar todas las reservas")
    void shouldListAllReservas() {

        ReservaModel reserva1 = crearReservaModel();
        ReservaModel reserva2 = crearReservaModel();
        reserva2.setId(2L);

        when(reservaRepository.findAll())
                .thenReturn(List.of(reserva1, reserva2));

        List<ReservaModel> resultado =
                reservaServices.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(reservaRepository).findAll();
    }

    @Test
    @DisplayName("Debe retornar reserva cuando el ID existe")
    void shouldReturnReservaWhenIdExists() {

        ReservaModel reserva = crearReservaModel();

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reserva));

        ReservaModel resultado =
                reservaServices.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getIdCliente());
        assertEquals(1L, resultado.getIdHabitacion());

        verify(reservaRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando reserva no existe")
    void shouldThrowExceptionWhenReservaDoesNotExist() {

        when(reservaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservaServices.buscarPorId(99L)
        );

        verify(reservaRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe modificar reserva correctamente")
    void shouldUpdateReservaSuccessfully() {

        ReservaDTO dto = crearDTO();
        dto.setFechaInicio(LocalDate.now().plusDays(2));
        dto.setFechaFin(LocalDate.now().plusDays(5));

        ReservaModel reservaExistente = crearReservaModel();

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reservaExistente));

        when(clienteClient.obtenerCliente(1L))
                .thenReturn(crearCliente());

        when(habitacionClient.obtenerHabitacion(1L))
                .thenReturn(crearHabitacionDisponible());

        when(reservaRepository.save(reservaExistente))
                .thenReturn(reservaExistente);

        ReservaModel resultado =
                reservaServices.modificar(1L, dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCliente());
        assertEquals(1L, resultado.getIdHabitacion());
        assertEquals(100000.0, resultado.getPrecioUnitario());
        assertEquals(300000.0, resultado.getMontoTotal());

        verify(reservaRepository).findById(1L);
        verify(clienteClient).obtenerCliente(1L);
        verify(habitacionClient).obtenerHabitacion(1L);
        verify(reservaRepository).save(reservaExistente);
    }

    @Test
    @DisplayName("Debe lanzar excepción al modificar reserva inexistente")
    void shouldThrowExceptionWhenUpdatingReservaDoesNotExist() {

        ReservaDTO dto = crearDTO();

        when(reservaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservaServices.modificar(99L, dto)
        );

        verify(reservaRepository).findById(99L);
        verify(reservaRepository, never()).save(any(ReservaModel.class));
        verifyNoInteractions(clienteClient, habitacionClient, notificacionClient);
    }

    @Test
    @DisplayName("Debe lanzar excepción al modificar con fecha inicio anterior a hoy")
    void shouldThrowExceptionWhenUpdatingFechaInicioIsBeforeToday() {

        ReservaDTO dto = crearDTO();
        dto.setFechaInicio(LocalDate.now().minusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(3));

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(crearReservaModel()));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> reservaServices.modificar(1L, dto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());

        verify(reservaRepository).findById(1L);
        verify(reservaRepository, never()).save(any(ReservaModel.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al modificar con fecha fin no posterior a fecha inicio")
    void shouldThrowExceptionWhenUpdatingFechaFinIsNotAfterFechaInicio() {

        ReservaDTO dto = crearDTO();
        dto.setFechaInicio(LocalDate.now().plusDays(3));
        dto.setFechaFin(LocalDate.now().plusDays(3));

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(crearReservaModel()));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> reservaServices.modificar(1L, dto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());

        verify(reservaRepository).findById(1L);
        verify(reservaRepository, never()).save(any(ReservaModel.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al modificar si cliente es null")
    void shouldThrowExceptionWhenUpdatingClienteDoesNotExist() {

        ReservaDTO dto = crearDTO();

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(crearReservaModel()));

        when(clienteClient.obtenerCliente(1L))
                .thenReturn(null);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> reservaServices.modificar(1L, dto)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());

        verify(clienteClient).obtenerCliente(1L);
        verify(reservaRepository, never()).save(any(ReservaModel.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al modificar si habitación es null")
    void shouldThrowExceptionWhenUpdatingHabitacionDoesNotExist() {

        ReservaDTO dto = crearDTO();

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(crearReservaModel()));

        when(clienteClient.obtenerCliente(1L))
                .thenReturn(crearCliente());

        when(habitacionClient.obtenerHabitacion(1L))
                .thenReturn(null);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> reservaServices.modificar(1L, dto)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());

        verify(clienteClient).obtenerCliente(1L);
        verify(habitacionClient).obtenerHabitacion(1L);
        verify(reservaRepository, never()).save(any(ReservaModel.class));
    }

    @Test
    @DisplayName("Debe eliminar reserva cuando existe")
    void shouldDeleteReservaWhenIdExists() {

        ReservaModel reserva = crearReservaModel();

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reserva));

        reservaServices.eliminar(1L);

        verify(reservaRepository).findById(1L);
        verify(reservaRepository).delete(reserva);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar reserva inexistente")
    void shouldThrowExceptionWhenDeletingReservaDoesNotExist() {

        when(reservaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservaServices.eliminar(99L)
        );

        verify(reservaRepository).findById(99L);
        verify(reservaRepository, never()).delete(any(ReservaModel.class));
    }

    private ReservaDTO crearDTO() {

        ReservaDTO dto = new ReservaDTO();
        dto.setIdCliente(1L);
        dto.setIdHabitacion(1L);
        dto.setFechaInicio(LocalDate.now().plusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(3));

        return dto;
    }

    private ReservaModel crearReservaModel() {

        ReservaModel reserva = new ReservaModel();
        reserva.setId(1L);
        reserva.setIdCliente(1L);
        reserva.setIdHabitacion(1L);
        reserva.setFechaInicio(LocalDate.now().plusDays(1));
        reserva.setFechaFin(LocalDate.now().plusDays(3));
        reserva.setPrecioUnitario(100000.0);
        reserva.setMontoTotal(200000.0);

        return reserva;
    }

    private ClienteDTO crearCliente() {

        ClienteDTO cliente = new ClienteDTO();
        cliente.setId(1L);

        return cliente;
    }

    private HabitacionDTO crearHabitacionDisponible() {

        HabitacionDTO habitacion = new HabitacionDTO();
        habitacion.setId(1L);
        habitacion.setDisponible(true);
        habitacion.setPrecioPorNoche(100000.0);

        return habitacion;
    }

    private HabitacionDTO crearHabitacionNoDisponible() {

        HabitacionDTO habitacion = new HabitacionDTO();
        habitacion.setId(1L);
        habitacion.setDisponible(false);
        habitacion.setPrecioPorNoche(100000.0);

        return habitacion;
    }
}