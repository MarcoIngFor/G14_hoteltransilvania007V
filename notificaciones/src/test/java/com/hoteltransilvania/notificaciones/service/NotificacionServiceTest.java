package com.hoteltransilvania.notificaciones.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.hoteltransilvania.notificaciones.dto.NotificacionDTO;
import com.hoteltransilvania.notificaciones.exception.ResourceNotFoundException;
import com.hoteltransilvania.notificaciones.model.NotificacionModel;
import com.hoteltransilvania.notificaciones.repository.NotificacionRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    @Test
    @DisplayName("Debe guardar notificación correctamente")
    void shouldSaveNotificacionSuccessfully() {

        NotificacionDTO dto = crearDTO();

        NotificacionModel guardada = crearModel(1L, "PENDIENTE");

        when(notificacionRepository.save(any(NotificacionModel.class)))
                .thenReturn(guardada);

        NotificacionModel resultado = notificacionService.guardar(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(10L, resultado.getIdCliente());
        assertEquals("mavis@hotel.com", resultado.getDestinatario());
        assertEquals("EMAIL", resultado.getCanal());
        assertEquals("Su reserva ha sido confirmada", resultado.getMensaje());
        assertEquals("PENDIENTE", resultado.getEstado());

        verify(notificacionRepository).save(any(NotificacionModel.class));
    }

    @Test
    @DisplayName("Debe listar todas las notificaciones")
    void shouldListAllNotificaciones() {

        NotificacionModel n1 = crearModel(1L, "PENDIENTE");
        NotificacionModel n2 = crearModel(2L, "ENVIADA");

        when(notificacionRepository.findAll())
                .thenReturn(List.of(n1, n2));

        List<NotificacionModel> resultado =
                notificacionService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(notificacionRepository).findAll();
    }

    @Test
    @DisplayName("Debe retornar notificación cuando el ID existe")
    void shouldReturnNotificacionWhenIdExists() {

        NotificacionModel notificacion = crearModel(1L, "PENDIENTE");

        when(notificacionRepository.findById(1L))
                .thenReturn(Optional.of(notificacion));

        NotificacionModel resultado =
                notificacionService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("PENDIENTE", resultado.getEstado());

        verify(notificacionRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando notificación no existe")
    void shouldThrowExceptionWhenNotificacionDoesNotExist() {

        when(notificacionRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> notificacionService.buscarPorId(99L)
        );

        verify(notificacionRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe buscar notificaciones por cliente")
    void shouldFindNotificacionesByCliente() {

        NotificacionModel notificacion = crearModel(1L, "PENDIENTE");

        when(notificacionRepository.findByIdCliente(10L))
                .thenReturn(List.of(notificacion));

        List<NotificacionModel> resultado =
                notificacionService.buscarPorCliente(10L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getIdCliente());

        verify(notificacionRepository).findByIdCliente(10L);
    }

    @Test
    @DisplayName("Debe buscar notificaciones por estado")
    void shouldFindNotificacionesByEstado() {

        NotificacionModel notificacion = crearModel(1L, "PENDIENTE");

        when(notificacionRepository.findByEstado("PENDIENTE"))
                .thenReturn(List.of(notificacion));

        List<NotificacionModel> resultado =
                notificacionService.buscarPorEstado("PENDIENTE");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getEstado());

        verify(notificacionRepository).findByEstado("PENDIENTE");
    }

    @Test
    @DisplayName("Debe actualizar notificación cuando existe")
    void shouldUpdateNotificacionWhenIdExists() {

        NotificacionModel existente = crearModel(1L, "PENDIENTE");

        NotificacionDTO dto = new NotificacionDTO();
        dto.setIdCliente(20L);
        dto.setDestinatario("dracula@hotel.com");
        dto.setCanal("SMS");
        dto.setMensaje("Su habitación está lista");

        when(notificacionRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(notificacionRepository.save(existente))
                .thenReturn(existente);

        NotificacionModel resultado =
                notificacionService.actualizar(1L, dto);

        assertNotNull(resultado);
        assertEquals(20L, resultado.getIdCliente());
        assertEquals("dracula@hotel.com", resultado.getDestinatario());
        assertEquals("SMS", resultado.getCanal());
        assertEquals("Su habitación está lista", resultado.getMensaje());

        verify(notificacionRepository).findById(1L);
        verify(notificacionRepository).save(existente);
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar notificación inexistente")
    void shouldThrowExceptionWhenUpdatingNotificacionDoesNotExist() {

        NotificacionDTO dto = crearDTO();

        when(notificacionRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> notificacionService.actualizar(99L, dto)
        );

        verify(notificacionRepository).findById(99L);
        verify(notificacionRepository, never()).save(any(NotificacionModel.class));
    }

    @Test
    @DisplayName("Debe marcar notificación como enviada")
    void shouldMarkNotificacionAsSent() {

        NotificacionModel existente = crearModel(1L, "PENDIENTE");

        when(notificacionRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(notificacionRepository.save(existente))
                .thenReturn(existente);

        NotificacionModel resultado =
                notificacionService.marcarComoEnviada(1L);

        assertNotNull(resultado);
        assertEquals("ENVIADA", resultado.getEstado());

        verify(notificacionRepository).findById(1L);
        verify(notificacionRepository).save(existente);
    }

    @Test
    @DisplayName("Debe lanzar excepción al marcar como enviada una notificación inexistente")
    void shouldThrowExceptionWhenMarkingAsSentNotificacionDoesNotExist() {

        when(notificacionRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> notificacionService.marcarComoEnviada(99L)
        );

        verify(notificacionRepository).findById(99L);
        verify(notificacionRepository, never()).save(any(NotificacionModel.class));
    }

    @Test
    @DisplayName("Debe eliminar notificación cuando existe")
    void shouldDeleteNotificacionWhenIdExists() {

        NotificacionModel notificacion = crearModel(1L, "PENDIENTE");

        when(notificacionRepository.findById(1L))
                .thenReturn(Optional.of(notificacion));

        notificacionService.eliminar(1L);

        verify(notificacionRepository).findById(1L);
        verify(notificacionRepository).delete(notificacion);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar notificación inexistente")
    void shouldThrowExceptionWhenDeletingNotificacionDoesNotExist() {

        when(notificacionRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> notificacionService.eliminar(99L)
        );

        verify(notificacionRepository).findById(99L);
        verify(notificacionRepository, never()).delete(any(NotificacionModel.class));
    }

    private NotificacionDTO crearDTO() {

        NotificacionDTO dto = new NotificacionDTO();
        dto.setIdCliente(10L);
        dto.setDestinatario("mavis@hotel.com");
        dto.setCanal("EMAIL");
        dto.setMensaje("Su reserva ha sido confirmada");

        return dto;
    }

    private NotificacionModel crearModel(Long id, String estado) {

        NotificacionModel notificacion = new NotificacionModel();
        notificacion.setId(id);
        notificacion.setIdCliente(10L);
        notificacion.setDestinatario("mavis@hotel.com");
        notificacion.setCanal("EMAIL");
        notificacion.setMensaje("Su reserva ha sido confirmada");
        notificacion.setEstado(estado);

        return notificacion;
    }
}