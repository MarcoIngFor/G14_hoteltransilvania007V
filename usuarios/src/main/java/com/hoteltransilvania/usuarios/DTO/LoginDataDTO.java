package com.hoteltransilvania.usuarios.DTO;

import com.hoteltransilvania.usuarios.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginDataDTO {

    private Usuario usuario;
    private List<PrivilegioDTO> privilegios;
}