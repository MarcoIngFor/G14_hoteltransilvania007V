package com.hoteltransilvania.mantenimiento.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para representar una habitación obtenida desde habitaciones-service")
public class HabitacionDTO {

    @Schema(description = "ID de la habitación", example = "1")
    private Long id;

    @Schema(description = "Número de habitación", example = "101")
    private Integer numero;

    @Schema(description = "Precio por noche", example = "85000")
    private Double precioPorNoche;

    @Schema(description = "Disponibilidad de la habitación", example = "true")
    private Boolean disponible;

    @Schema(description = "Tipo de habitación", example = "Suite")
    private String tipo;
}