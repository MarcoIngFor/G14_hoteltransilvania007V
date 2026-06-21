package com.hoteltransilvania.reservas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para representar clientes obtenidos desde clientes-service")
public class ClienteDTO {

    @Schema(description = "ID del cliente", example = "1")
    private Long id;
}