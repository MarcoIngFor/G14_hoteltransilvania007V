package com.hoteltransilvania.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private Long id;
    private String username;
    private Long rolId;
    private String mensaje;
}