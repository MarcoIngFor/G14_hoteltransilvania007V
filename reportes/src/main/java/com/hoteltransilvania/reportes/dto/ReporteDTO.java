package com.hoteltransilvania.reportes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para registrar y actualizar reportes del sistema")
public class ReporteDTO {

    @Schema(description = "Tipo de reporte", example = "RESERVAS")
    @NotBlank(message = "El tipo de reporte es obligatorio")
    private String tipo;

    @Schema(description = "Descripción del reporte", example = "Reporte mensual de reservas e ingresos")
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @Schema(description = "Total de reservas registradas en el reporte", example = "25")
    @NotNull(message = "El total de reservas es obligatorio")
    @PositiveOrZero(message = "El total de reservas no puede ser negativo")
    private Integer totalReservas;

    @Schema(description = "Total de ingresos registrados en el reporte", example = "1250000.0")
    @NotNull(message = "El total de ingresos es obligatorio")
    @PositiveOrZero(message = "El total de ingresos no puede ser negativo")
    private Double totalIngresos;
}