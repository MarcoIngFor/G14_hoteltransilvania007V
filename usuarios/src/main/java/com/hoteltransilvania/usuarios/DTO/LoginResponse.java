package com.hoteltransilvania.usuarios.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private Long id;
    private String username;
    private Long rolId;
    private String token;
    private String mensaje;
}