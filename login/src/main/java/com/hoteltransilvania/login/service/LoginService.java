package com.hoteltransilvania.login.service;

import com.hoteltransilvania.login.DTO.LoginRequest;
import com.hoteltransilvania.login.DTO.LoginResponse;
import com.hoteltransilvania.login.DTO.UsuarioResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class LoginService {

    private final RestClient restClient;

    @Value("${usuarios-service.url}")
    private String usuariosServiceUrl;

    public LoginService(RestClient restClient) {
        this.restClient = restClient;
    }

    public LoginResponse login(LoginRequest request) {

        UsuarioResponse usuario = restClient.get()
                .uri(usuariosServiceUrl + "/usuarios")
                .retrieve()
                .body(UsuarioResponse[].class)[0];

        if (!usuario.getUsername().equals(request.getUsername())) {
            throw new RuntimeException("Usuario incorrecto");
        }

        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return new LoginResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRolId(),
                "Login exitoso"
        );
    }
}