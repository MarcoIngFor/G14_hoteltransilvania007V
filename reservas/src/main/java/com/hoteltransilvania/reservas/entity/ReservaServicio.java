package com.hoteltransilvania.reservas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reserva_servicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reservaId;

    private Long servicioId;

    private int cantidad;

    private double subtotal;
}