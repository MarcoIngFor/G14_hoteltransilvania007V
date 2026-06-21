package com.hoteltransilvania.mantenimiento.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para registrar y actualizar mantenimientos de habitaciones")
public class MantenimientoDTO {

    @Schema(description = "ID de la habitación asociada al mantenimiento", example = "1")
    @NotNull(message = "El ID de habitación no puede estar vacío")
    @Positive(message = "El ID de habitación debe ser mayor a 0")
    private Long idHabitacion;

    @Schema(description = "Descripción del mantenimiento", example = "Reparación de aire acondicionado")
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @Schema(description = "Fecha de inicio del mantenimiento", example = "2026-06-20")
    @NotNull(message = "La fecha de inicio no puede estar vacía")
    @FutureOrPresent(message = "La fecha de inicio no puede ser anterior a hoy")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de término del mantenimiento", example = "2026-06-22")
    @NotNull(message = "La fecha de fin no puede estar vacía")
    @FutureOrPresent(message = "La fecha de fin no puede ser anterior a hoy")
    private LocalDate fechaFin;

    @Schema(description = "Estado del mantenimiento", example = "PENDIENTE")
    @NotBlank(message = "Debe especificar un estado")
    private String estado;
}