package com.hoteltransilvania.reservas.dto;

import lombok.Data;

@Data
public class HabitacionDTO {

    private Long id;
    private String numero;
    private String tipo;
    private double precioPorNoche;
    private boolean disponible;

}