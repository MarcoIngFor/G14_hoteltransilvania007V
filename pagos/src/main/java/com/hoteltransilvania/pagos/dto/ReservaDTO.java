package com.hoteltransilvania.pagos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para representar una reserva obtenida desde reservas-service")
public class ReservaDTO {

    @Schema(description = "ID de la reserva", example = "1")
    private Long id;

    @Schema(description = "ID del cliente", example = "1")
    private Long idCliente;

    @Schema(description = "ID de la habitación", example = "1")
    private Long idHabitacion;

    @Schema(description = "Fecha de inicio de la reserva", example = "2026-06-20")
    private String fechaInicio;

    @Schema(description = "Fecha de término de la reserva", example = "2026-06-22")
    private String fechaFin;

    @Schema(description = "Monto total de la reserva", example = "170000")
    private Double montoTotal;
}