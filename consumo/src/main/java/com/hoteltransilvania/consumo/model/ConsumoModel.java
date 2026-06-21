package com.hoteltransilvania.consumo.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="consumo")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ConsumoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idServicioExtra;
    private Long idReserva;
    private Long idCliente;
    private Long idHabitacion;
    private int cantidad;
    private double totalConsumo;
    private LocalDate fecha;
}
