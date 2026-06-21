package com.hoteltransilvania.reservas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para representar habitaciones obtenidas desde habitaciones-service")
public class HabitacionDTO {

    @Schema(description = "ID de la habitación", example = "1")
    private Long id;

    @Schema(description = "Disponibilidad de la habitación", example = "true")
    private boolean disponible;

    @Schema(description = "Precio por noche", example = "85000")
    private double precioPorNoche;
}