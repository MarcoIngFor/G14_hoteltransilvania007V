package com.hoteltransilvania.pagos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para representar consumos obtenidos desde consumo-service")
public class ConsumoDTO {

    @Schema(description = "ID del consumo", example = "1")
    private Long id;

    @Schema(description = "ID de la reserva asociada", example = "1")
    private Long idReserva;

    @Schema(description = "ID del cliente asociado", example = "1")
    private Long idCliente;

    @Schema(description = "ID de la habitación asociada", example = "1")
    private Long idHabitacion;

    @Schema(description = "Cantidad consumida", example = "2")
    private int cantidad;

    @Schema(description = "Total del consumo", example = "17000")
    private double totalConsumo;
}