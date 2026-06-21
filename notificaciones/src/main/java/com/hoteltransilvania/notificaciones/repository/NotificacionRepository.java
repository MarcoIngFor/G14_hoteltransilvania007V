package com.hoteltransilvania.notificaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoteltransilvania.notificaciones.model.NotificacionModel;

@Repository
public interface NotificacionRepository extends JpaRepository<NotificacionModel, Long> {
    List<NotificacionModel> findByIdCliente(Long idCliente);
    List<NotificacionModel> findByEstado(String estado);
}
