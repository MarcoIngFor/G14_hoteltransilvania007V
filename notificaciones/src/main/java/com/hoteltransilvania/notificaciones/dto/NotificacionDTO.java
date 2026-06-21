package com.hoteltransilvania.notificaciones.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para crear notificaciones dirigidas a clientes")
public class NotificacionDTO {

    @Schema(description = "ID del cliente destinatario", example = "1")
    @NotNull(message = "El ID del cliente es obligatorio")
    @Positive(message = "El ID del cliente debe ser mayor a 0")
    private Long idCliente;

    @Schema(description = "Destinatario de la notificación", example = "mavis@hotel.com")
    @NotBlank(message = "El destinatario es obligatorio")
    private String destinatario;

    @Schema(description = "Canal de envío de la notificación", example = "EMAIL")
    @NotBlank(message = "El canal es obligatorio")
    private String canal;

    @Schema(description = "Mensaje de la notificación", example = "Su reserva ha sido confirmada")
    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
}