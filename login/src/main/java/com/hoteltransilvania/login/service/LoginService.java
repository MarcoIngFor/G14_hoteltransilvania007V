package com.hoteltransilvania.login.service;

import com.hoteltransilvania.login.DTO.LoginRequest;
import com.hoteltransilvania.login.DTO.LoginResponse;
import com.hoteltransilvania.login.DTO.UsuarioResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Base64;

@Service
public class LoginService {

    private final RestClient restClient;

    @Value("${usuarios-service.url}")
    private String usuariosServiceUrl;

    public LoginService(RestClient restClient) {
        this.restClient = restClient;
    }

    public LoginResponse login(LoginRequest request) {

        String auth = Base64.getEncoder()
                .encodeToString("admin:1234".getBytes());

        UsuarioResponse[] usuarios = restClient.get()
                .uri(usuariosServiceUrl + "/usuarios")
                .header("Authorization", "Basic " + auth)
                .retrieve()
                .body(UsuarioResponse[].class);

        UsuarioResponse usuarioEncontrado = null;

        for (UsuarioResponse usuario : usuarios) {

            if (usuario.getUsername().equals(request.getUsername())) {
                usuarioEncontrado = usuario;
                break;
            }
        }

        if (usuarioEncontrado == null) {
            throw new RuntimeException("Usuario incorrecto");
        }

        if (!usuarioEncontrado.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return new LoginResponse(
                usuarioEncontrado.getId(),
                usuarioEncontrado.getUsername(),
                usuarioEncontrado.getRolId(),
                "Login exitoso"
        );
    }
}