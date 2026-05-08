package com.hoteltransilvania.usuarios.DTO;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;
}