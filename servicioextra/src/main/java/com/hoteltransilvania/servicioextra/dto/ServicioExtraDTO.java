package com.hoteltransilvania.servicioextra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para registrar y actualizar servicios extra")
public class ServicioExtraDTO {

    @Schema(description = "Nombre del servicio extra", example = "Desayuno buffet")
    @NotBlank(message = "Debe especificar nombre del servicio")
    private String nombre;

    @Schema(description = "Descripción del servicio extra", example = "Desayuno buffet disponible para huéspedes")
    @NotBlank(message = "Debe especificar descripcion del servicio")
    private String descripcion;

    @Schema(description = "Costo del servicio extra", example = "8500")
    @NotNull(message = "Debe Especificar Costo del Servicio")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double precio;

    @Schema(description = "ID del tipo de servicio asociado", example = "1")
    @NotNull(message = "Debe especificar el ID del tipo de servicio")
    @Positive(message = "El ID del tipo de servicio debe ser mayor a 0")
    private Long idTipoServicio;
}