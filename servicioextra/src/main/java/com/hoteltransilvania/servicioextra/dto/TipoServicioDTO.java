package com.hoteltransilvania.servicioextra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para registrar tipos de servicio")
public class TipoServicioDTO {

    @Schema(description = "Nombre del tipo de servicio", example = "Alimentación")
    @NotBlank(message = "El nombre es Obligatorio...")
    private String nombre;
}