package com.hoteltransilvania.consumo.dto.RespuestaDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ConsumoRespuestaDTO {

    private String message;
    private Long idServicioExtra;
    private Long idCliente;
    private Long idReserva;
}
