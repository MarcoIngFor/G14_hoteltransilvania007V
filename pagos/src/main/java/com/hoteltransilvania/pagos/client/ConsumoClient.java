package com.hoteltransilvania.pagos.client;

import com.hoteltransilvania.pagos.config.FeignConfig;
import com.hoteltransilvania.pagos.dto.ConsumoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "servicioextra-service", url = "${consumos.service.url}",configuration = FeignConfig.class)
public interface ConsumoClient {

    @GetMapping("/consumo/reserva/{idReserva}")
    List<ConsumoDTO> obtenerConsumosReserva(@PathVariable Long idReserva);
}