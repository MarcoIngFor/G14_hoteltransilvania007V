package com.hoteltransilvania.usuarios.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor


public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "el username no puede estar vacío")
    @Pattern(
            regexp = "^[a-zA-Z0-9]+$",
            message = "el username solo puede tener letras y números"
    )
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "la contraseña no puede estar vacía")
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Long rolId;



}
