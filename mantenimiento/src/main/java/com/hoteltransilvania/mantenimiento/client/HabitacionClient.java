package com.hoteltransilvania.mantenimiento.client;

import com.hoteltransilvania.mantenimiento.config.FeignConfig;
import com.hoteltransilvania.mantenimiento.dto.EstadoHabitacionDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "habitaciones-service",
        url = "${habitaciones.service.url}",
        configuration = FeignConfig.class
)
public interface HabitacionClient {

    @PutMapping("/habitaciones/estado/{id}")
    void actualizarEstadoHabitacion(
            @PathVariable("id") Long id,
            @RequestBody EstadoHabitacionDTO disponible
    );
}