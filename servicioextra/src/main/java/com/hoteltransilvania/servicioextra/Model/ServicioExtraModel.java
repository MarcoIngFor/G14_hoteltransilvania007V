package com.hoteltransilvania.servicioextra.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "servicoextra")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ServicioExtraModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nombre;
    private String descripcion;
    private double precio;
    
    @ManyToOne
    @JoinColumn(name = "id_tiposervicio") // Usamos el nombre de la tabla + id para que sea claro
    private TipoServicioModel tipoServicio;
}
