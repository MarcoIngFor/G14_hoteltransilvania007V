package com.hoteltransilvania.habitaciones.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para registrar y actualizar habitaciones")
public class HabitacionDTO {

    @Schema(description = "Número de habitación", example = "101")
    @NotNull(message = "El número de habitación no puede estar vacío")
    @Positive(message = "El número debe ser positivo")
    private Integer numero;

    @Schema(description = "Tipo de habitación", example = "Suite")
    @NotNull(message = "Debe especificar el tipo de habitación")
    private String tipo;

    @Schema(description = "Precio por noche", example = "85000")
    @NotNull(message = "Debe ingresar precio por noche")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precioPorNoche;

    @Schema(description = "Disponibilidad de la habitación", example = "true")
    private boolean disponible;
}