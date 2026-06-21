package com.hoteltransilvania.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "DTO utilizado para la creación y actualización de usuarios")
public class UsuarioDTO {

    @Schema(
        description = "Nombre del usuario",
        example = "Marco"
    )
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(
        description = "Apellido del usuario",
        example = "Castro"
    )
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Schema(
        description = "Nombre de usuario para iniciar sesión",
        example = "mcastro"
    )
    @NotBlank(message = "El username es obligatorio")
    private String username;

    @Schema(
        description = "Contraseña del usuario",
        example = "Admin123*"
    )
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @Schema(
        description = "Correo electrónico del usuario",
        example = "marco@email.com"
    )
    @Email(message = "Correo inválido")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @Schema(
        description = "Rol asignado al usuario",
        example = "ADMIN",
        allowableValues = {"ADMIN", "RECEPCIONISTA"}
    )
    @NotBlank(message = "El rol es obligatorio")
    private String rol;
}