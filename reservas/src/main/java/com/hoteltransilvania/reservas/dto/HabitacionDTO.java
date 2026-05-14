package com.hoteltransilvania.reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionDTO {

    private Long id;

    private String numero;

    private String tipo;

    private Double precioPorNoche;

    private Boolean disponible;
}