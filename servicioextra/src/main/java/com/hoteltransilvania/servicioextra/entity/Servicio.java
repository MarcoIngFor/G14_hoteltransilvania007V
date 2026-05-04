package com.hoteltransilvania.servicioextra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "servicios_adicionales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    private String descripcion;

    @Min(value = 1, message = "El precio debe ser mayor a 0")
    private double precio;

    private boolean activo;
}
