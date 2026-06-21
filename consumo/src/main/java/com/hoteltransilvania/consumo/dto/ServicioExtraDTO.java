package com.hoteltransilvania.consumo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para representar servicios extra disponibles para consumo")
public class ServicioExtraDTO {

    @Schema(
            description = "ID del servicio extra",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nombre del servicio extra",
            example = "Desayuno buffet"
    )
    private String nombre;

    @Schema(
            description = "Precio del servicio extra",
            example = "8500"
    )
    private Double precio;
}