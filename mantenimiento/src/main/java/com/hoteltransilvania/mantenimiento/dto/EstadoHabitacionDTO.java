package com.hoteltransilvania.mantenimiento.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para actualizar la disponibilidad de una habitación desde mantenimiento")
public class EstadoHabitacionDTO {

    @Schema(description = "Estado de disponibilidad de la habitación", example = "false")
    private boolean disponible;
}