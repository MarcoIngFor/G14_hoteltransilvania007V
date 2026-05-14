package com.hoteltransilvania.privilegios.client;

import com.hoteltransilvania.privilegios.DTO.UsuarioRolDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class RolClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<UsuarioRolDTO> obtenerRolesUsuario(Long usuarioId) {

        String url = "http://roles-service:8092/roles/usuarios/" + usuarioId;

        UsuarioRolDTO[] roles = restTemplate.getForObject(
                url,
                UsuarioRolDTO[].class
        );

        return Arrays.asList(roles);
    }
}