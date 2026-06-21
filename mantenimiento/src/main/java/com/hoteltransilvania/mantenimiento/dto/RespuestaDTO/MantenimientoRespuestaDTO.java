package com.hoteltransilvania.mantenimiento.dto.RespuestaDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MantenimientoRespuestaDTO {

    private String mensaje;
    private String descripcion;
    private String estado;
    private Long idhabitacion;

}
