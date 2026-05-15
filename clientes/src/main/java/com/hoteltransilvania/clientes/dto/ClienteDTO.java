package com.hoteltransilvania.clientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class ClienteDTO {

    // Aquí NO ponemos ID, porque el usuario no debe enviarlo
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato inválido")
    private String correo;

    private String telefono;
}

