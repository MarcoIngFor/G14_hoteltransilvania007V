package com.hoteltransilvania.privilegios.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rol_privilegio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolPrivilegio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rol_id", nullable = false)
    private Long rolId;

    @Column(name = "privilegio_id", nullable = false)
    private Long privilegioId;
}