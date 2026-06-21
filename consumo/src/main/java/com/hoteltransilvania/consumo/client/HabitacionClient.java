package com.hoteltransilvania.consumo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hoteltransilvania.consumo.config.FeignConfig;

@FeignClient(
        name = "habitacion-service",
        url = "${habitacion.service.url}",
        configuration = FeignConfig.class
)
public interface HabitacionClient {

    @GetMapping("/habitaciones/{id}")
    Object buscarPorId(@PathVariable("id") Long id);

}