package com.hoteltransilvania.pagos.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PagosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idReserva;

    private double subtotalHabitacion;

    private double subtotalServicios;

    private double totalPagar;
    
    private String metodoPago;

    private String estadoPago;

    private LocalDate fechaPago;
}
