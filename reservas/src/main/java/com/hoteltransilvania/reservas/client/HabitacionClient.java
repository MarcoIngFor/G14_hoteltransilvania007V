package com.hoteltransilvania.reservas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hoteltransilvania.reservas.config.FeignConfig;
import com.hoteltransilvania.reservas.dto.HabitacionDTO;

@FeignClient(name = "habitaciones-service", url = "${habitaciones.service.url}",configuration = FeignConfig.class)
public interface HabitacionClient {

    @GetMapping("/habitaciones/{id}")
    HabitacionDTO obtenerHabitacion(@PathVariable("id") Long id);
}