package com.hoteltransilvania.habitacionesservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "habitaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de habitación no puede estar vacío")
    private String numero;

    @NotBlank(message = "El tipo de habitación no puede estar vacío")
    private String tipo;

    @Positive(message = "El precio debe ser mayor a 0")
    private double precioPorNoche;

    private boolean disponible;
}