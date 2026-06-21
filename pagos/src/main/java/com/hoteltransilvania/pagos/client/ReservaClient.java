package com.hoteltransilvania.pagos.client;

import com.hoteltransilvania.pagos.config.FeignConfig;
import com.hoteltransilvania.pagos.dto.ReservaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reservas-service", url = "${reservas.service.url}",configuration = FeignConfig.class)
public interface ReservaClient {

    @GetMapping("/reservas/{id}")
    ReservaDTO obtenerReserva(@PathVariable Long id);
}