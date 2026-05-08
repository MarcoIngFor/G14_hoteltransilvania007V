package com.hoteltransilvania.login.DTO;

import lombok.Data;

@Data
public class UsuarioResponse {

    private Long id;
    private String username;
    private String password;
    private Long rolId;
}