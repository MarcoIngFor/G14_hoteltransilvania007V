package com.hoteltransilvania.habitaciones.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data

public class HabitacionDTO {

    @Positive(message = "El numero debe ser positivo")
    private Integer numero;

    @NotNull(message = "Debe Especificar el tipo de habitacion")
    private String tipo;

    @Positive(message = "el precio debe ser un numero entero positivo")
    private double precioPorNoche;

    private boolean disponible;

}
