package com.hoteltransilvania.reservas.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para crear y actualizar reservas")
public class ReservaDTO {

    @Schema(description = "ID del cliente asociado a la reserva", example = "1")
    @NotNull(message = "El ID Cliente no puede estar vacío")
    @Positive(message = "El ID debe ser mayor a 0")
    private Long idCliente;

    @Schema(description = "ID de la habitación asociada a la reserva", example = "1")
    @NotNull(message = "El ID Habitación no puede estar vacío")
    @Positive(message = "El ID debe ser mayor a 0")
    private Long idHabitacion;

    @Schema(description = "Fecha de entrada de la reserva", example = "20-06-2026")
    @NotNull(message = "Debe ingresarse una fecha de entrada")
    @FutureOrPresent(message = "La fecha de entrada no puede ser anterior a hoy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de salida de la reserva", example = "22-06-2026")
    @NotNull(message = "Debe ingresarse una fecha de salida")
    @FutureOrPresent(message = "La fecha de salida no puede ser anterior a hoy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaFin;
}