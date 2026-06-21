package com.hoteltransilvania.reservas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hoteltransilvania.reservas.config.FeignConfig;
import com.hoteltransilvania.reservas.dto.NotificacionDTO;

@FeignClient(name = "notificaciones-service", url = "${notificaciones.service.url}", configuration = FeignConfig.class)
public interface NotificacionClient {

    @PostMapping("/notificaciones/registrar")
    void registrarNotificacion(@RequestBody NotificacionDTO dto);
}