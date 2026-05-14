package com.hoteltransilvania.usuarios.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios_sesiones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioSesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del usuario que inició sesión
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    // Nombre o username del usuario logeado
    @Column(nullable = false)
    private String username;

    // Fecha y hora en que inició sesión
    @Column(name = "fecha_login", nullable = false)
    private LocalDateTime fechaLogin;

    // Indica si la sesión sigue activa
    @Column(nullable = false)
    private Boolean activo;
}