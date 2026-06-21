package com.hoteltransilvania.habitaciones.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO utilizado para actualizar la disponibilidad de una habitación")
public class EstadoHabitacionDTO {

    @Schema(description = "Estado de disponibilidad de la habitación", example = "false")
    private boolean disponible;
}