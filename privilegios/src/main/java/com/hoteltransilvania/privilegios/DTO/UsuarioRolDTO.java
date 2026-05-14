package com.hoteltransilvania.privilegios.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRolDTO {

    private Long id;
    private Long usuarioId;
    private Long rolId;
}