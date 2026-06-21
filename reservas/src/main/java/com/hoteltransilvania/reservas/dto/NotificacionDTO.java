package com.hoteltransilvania.reservas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para enviar notificaciones desde reservas-service")
public class NotificacionDTO {

    @Schema(description = "ID del cliente destinatario", example = "1")
    private Long idCliente;

    @Schema(description = "Destinatario de la notificación", example = "cliente@email.com")
    private String destinatario;

    @Schema(description = "Canal de envío", example = "EMAIL")
    private String canal;

    @Schema(description = "Mensaje de la notificación", example = "Su reserva ha sido registrada correctamente")
    private String mensaje;
}