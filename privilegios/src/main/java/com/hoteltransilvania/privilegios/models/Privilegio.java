package com.hoteltransilvania.privilegios.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "privilegios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Privilegio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del privilegio es obligatorio")
    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;
}
