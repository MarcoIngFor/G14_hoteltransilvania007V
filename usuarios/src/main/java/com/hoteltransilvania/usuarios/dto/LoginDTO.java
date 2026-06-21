package com.hoteltransilvania.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @Schema(
        description = "Nombre del usuario",
        example = "Marco"
    )
    @NotBlank(message = "Debe ingresar Usuario")
    private String username;

    @Schema(
        description = "Contraseña del usuario",
        example = "Admin123*"
    )
    @NotBlank(message = "Debe Ingresar Password")
    private String password;
}
