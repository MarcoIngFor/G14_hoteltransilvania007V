package com.hoteltransilvania.privilegios.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsignarPrivilegioRequest {

    private Long rolId;
    private Long privilegioId;
}