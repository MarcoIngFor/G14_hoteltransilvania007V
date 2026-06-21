package com.hoteltransilvania.consumo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para registrar y actualizar consumos asociados a una reserva")
public class ConsumoDTO {

    @Schema(description = "ID del servicio extra consumido", example = "1")
    @NotNull(message = "El ID del servicio extra no puede estar vacío")
    @Positive(message = "El ID del servicio extra debe ser mayor a 0")
    private Long idServicioExtra;

    @Schema(description = "ID de la reserva asociada al consumo", example = "1")
    @NotNull(message = "El ID de la reserva no puede estar vacío")
    @Positive(message = "El ID de la reserva debe ser mayor a 0")
    private Long idReserva;

    @Schema(description = "ID del cliente asociado al consumo", example = "1")
    @NotNull(message = "El ID del cliente no puede estar vacío")
    @Positive(message = "El ID del cliente debe ser mayor a 0")
    private Long idCliente;

    @Schema(description = "ID de la habitación asociada al consumo", example = "1")
    @NotNull(message = "El ID de la habitación no puede estar vacío")
    @Positive(message = "El ID de la habitación debe ser mayor a 0")
    private Long idHabitacion;

    @Schema(description = "Cantidad consumida del servicio extra", example = "2")
    @Positive(message = "La cantidad debe ser un número mayor a 0")
    private int cantidad;
}