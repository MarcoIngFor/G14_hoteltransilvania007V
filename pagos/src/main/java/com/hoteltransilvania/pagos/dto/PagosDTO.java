package com.hoteltransilvania.pagos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para registrar y actualizar pagos")
public class PagosDTO {

    @Schema(description = "ID de la reserva asociada al pago", example = "1")
    @NotNull(message = "El ID Reserva no puede estar vacío")
    @Positive(message = "El ID debe ser mayor a 0")
    private Long idReserva;

    @Schema(description = "Método de pago utilizado", example = "TARJETA")
    @NotBlank(message = "Debe especificar el método de pago")
    private String metodoPago;
}