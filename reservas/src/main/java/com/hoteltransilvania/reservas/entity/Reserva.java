package com.hoteltransilvania.reservas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive(message = "El ID de Cliente debe ser mayor a 0")
    private Long clienteId;

    @Positive(message = "El ID de Habitacion debe ser mayor a 0")
    private Long habitacionId;

    @Positive(message = "El ID de Servicio debe ser mayor a 0")
    private Long ServicioId;

    private LocalDate fechaEntrada;

    private LocalDate fechaSalida;

    private String estado;
}