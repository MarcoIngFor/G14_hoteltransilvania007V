package com.hoteltransilvania.consumo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hoteltransilvania.consumo.config.FeignConfig;

@FeignClient(name = "reserva-service", url = "${reserva.service.url}",
        configuration = FeignConfig.class)
public interface ReservaClient {

    @GetMapping("/reservas/{id}")
    Object buscarPorId(@PathVariable("id") Long id);
}