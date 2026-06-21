package com.hoteltransilvania.consumo.client;

import com.hoteltransilvania.consumo.config.FeignConfig;
import com.hoteltransilvania.consumo.dto.ServicioExtraDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicioextra-service",url = "${servicioextra.service.url}",
        configuration = FeignConfig.class)


public interface ServicioExtraClient {

    @GetMapping("/servicioextra/listar/{id}")
    ServicioExtraDTO buscarPorId(@PathVariable("id") Long id);
}