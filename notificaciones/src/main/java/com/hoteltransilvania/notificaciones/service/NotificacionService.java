package com.hoteltransilvania.notificaciones.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hoteltransilvania.notificaciones.dto.NotificacionDTO;
import com.hoteltransilvania.notificaciones.exception.ResourceNotFoundException;
import com.hoteltransilvania.notificaciones.model.NotificacionModel;
import com.hoteltransilvania.notificaciones.repository.NotificacionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public NotificacionModel guardar(NotificacionDTO dto) {
        log.info("Registrando notificación para cliente ID: {}", dto.getIdCliente());
        NotificacionModel notificacion = new NotificacionModel();
        notificacion.setIdCliente(dto.getIdCliente());
        notificacion.setDestinatario(dto.getDestinatario());
        notificacion.setCanal(dto.getCanal());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setEstado("PENDIENTE");
        notificacion.setFechaCreacion(LocalDateTime.now());
        NotificacionModel guardada = notificacionRepository.save(notificacion);
        log.info("Notificación registrada correctamente ID: {}", guardada.getId());
        return guardada;
    }

    public List<NotificacionModel> listarTodos() {
        log.info("Listando todas las notificaciones");
        return notificacionRepository.findAll();
    }

    public NotificacionModel buscarPorId(Long id) {
        log.info("Buscando notificación ID: {}", id);
        return notificacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notificación no encontrada ID: {}", id);
                    return new ResourceNotFoundException("Notificación no encontrada con ID: " + id);
                });
    }

    public List<NotificacionModel> buscarPorCliente(Long idCliente) {
        log.info("Buscando notificaciones por cliente ID: {}", idCliente);
        return notificacionRepository.findByIdCliente(idCliente);
    }

    public List<NotificacionModel> buscarPorEstado(String estado) {
        log.info("Buscando notificaciones por estado: {}", estado);
        return notificacionRepository.findByEstado(estado);
    }

    public NotificacionModel actualizar(Long id, NotificacionDTO dto) {
        log.info("Actualizando notificación ID: {}", id);
        NotificacionModel notificacion = buscarPorId(id);
        notificacion.setIdCliente(dto.getIdCliente());
        notificacion.setDestinatario(dto.getDestinatario());
        notificacion.setCanal(dto.getCanal());
        notificacion.setMensaje(dto.getMensaje());
        NotificacionModel actualizada = notificacionRepository.save(notificacion);
        log.info("Notificación actualizada correctamente ID: {}", id);
        return actualizada;
    }

    public NotificacionModel marcarComoEnviada(Long id) {
        log.info("Marcando notificación como enviada ID: {}", id);
        NotificacionModel notificacion = buscarPorId(id);
        notificacion.setEstado("ENVIADA");
        NotificacionModel actualizada = notificacionRepository.save(notificacion);
        log.info("Notificación marcada como enviada ID: {}", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        log.warn("Eliminando notificación ID: {}", id);
        NotificacionModel notificacion = buscarPorId(id);
        notificacionRepository.delete(notificacion);
        log.info("Notificación eliminada correctamente ID: {}", id);
    }
}
