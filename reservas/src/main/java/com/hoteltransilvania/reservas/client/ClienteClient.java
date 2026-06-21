package com.hoteltransilvania.reservas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hoteltransilvania.reservas.config.FeignConfig;
import com.hoteltransilvania.reservas.dto.ClienteDTO;

@FeignClient(name = "clientes-service", url = "${clientes.service.url}",configuration = FeignConfig.class)

public interface ClienteClient {

    @GetMapping("/clientes/{id}")
    ClienteDTO obtenerCliente(@PathVariable("id") Long id);
}