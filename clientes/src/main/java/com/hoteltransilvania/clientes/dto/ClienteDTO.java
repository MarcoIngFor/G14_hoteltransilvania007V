package com.hoteltransilvania.clientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para crear y actualizar clientes")
public class ClienteDTO {

    @Schema(
            description = "Nombre del cliente",
            example = "Jonathan"
    )
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(
            description = "Apellido del cliente",
            example = "Harker"
    )
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Schema(
            description = "Correo electrónico del cliente",
            example = "jonathan.harker@email.com"
    )
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato inválido")
    private String correo;

    @Schema(
            description = "Teléfono de contacto del cliente",
            example = "+56912345678"
    )
    @Pattern(
            regexp = "^$|^[0-9+ ]{8,15}$",
            message = "El teléfono debe tener un formato válido"
    )
    private String telefono;
}