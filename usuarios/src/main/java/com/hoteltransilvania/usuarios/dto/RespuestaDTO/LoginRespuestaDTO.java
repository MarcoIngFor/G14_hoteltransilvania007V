package com.hoteltransilvania.usuarios.dto.RespuestaDTO;

import lombok.Data;

@Data
public class LoginRespuestaDTO {

    private String token;
    private String rol;
    private String mensaje;
}