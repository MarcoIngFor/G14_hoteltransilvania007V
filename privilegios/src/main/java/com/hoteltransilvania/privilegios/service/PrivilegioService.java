package com.hoteltransilvania.privilegios.service;

import com.hoteltransilvania.privilegios.DTO.AsignarPrivilegioRequest;
import com.hoteltransilvania.privilegios.DTO.UsuarioRolDTO;
import com.hoteltransilvania.privilegios.client.RolClient;
import com.hoteltransilvania.privilegios.models.Privilegio;
import com.hoteltransilvania.privilegios.models.RolPrivilegio;
import com.hoteltransilvania.privilegios.repository.PrivilegioRepository;
import com.hoteltransilvania.privilegios.repository.RolPrivilegioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrivilegioService {

    // ========================================
    // REPOSITORY DE PRIVILEGIOS
    // ========================================
    @Autowired
    private PrivilegioRepository privilegioRepository;

    // ========================================
    // REPOSITORY DE RELACIÓN ROL-PRIVILEGIO
    // ========================================
    @Autowired
    private RolPrivilegioRepository rolPrivilegioRepository;

    // ========================================
    // CLIENTE PARA CONSULTAR ROLES
    // ========================================
    // Consulta el microservicio de roles
    // para obtener los roles de un usuario
    @Autowired
    private RolClient rolClient;

    // ========================================
    // LISTAR TODOS LOS PRIVILEGIOS
    // ========================================
    public List<Privilegio> listar() {

        return privilegioRepository.findAll();
    }

    // ========================================
    // OBTENER PRIVILEGIO POR ID
    // ========================================
    public Privilegio obtenerPorId(Long id) {

        return privilegioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Privilegio no encontrado"
                        ));
    }

    // ========================================
    // CREAR PRIVILEGIO
    // ========================================
    public Privilegio guardar(
            Privilegio privilegio
    ) {

        return privilegioRepository.save(privilegio);
    }

    // ========================================
    // ASIGNAR PRIVILEGIO A UN ROL
    // ========================================
    public RolPrivilegio asignarPrivilegio(
            AsignarPrivilegioRequest request
    ) {

        RolPrivilegio rolPrivilegio =
                RolPrivilegio.builder()
                        .rolId(request.getRolId())
                        .privilegioId(
                                request.getPrivilegioId()
                        )
                        .build();

        return rolPrivilegioRepository
                .save(rolPrivilegio);
    }

    // ========================================
    // OBTENER PRIVILEGIOS DE UN ROL
    // ========================================
    public List<Privilegio> obtenerPrivilegiosRol(
            Long rolId
    ) {

        // Buscar relaciones rol-privilegio
        List<RolPrivilegio> relaciones =
                rolPrivilegioRepository
                        .findByRolId(rolId);

        // Lista final de privilegios
        List<Privilegio> privilegios =
                new ArrayList<>();

        // Recorrer relaciones
        for (RolPrivilegio relacion : relaciones) {

            Privilegio privilegio =
                    privilegioRepository.findById(
                            relacion.getPrivilegioId()
                    ).orElse(null);

            // Si existe, agregarlo
            if (privilegio != null) {
                privilegios.add(privilegio);
            }
        }

        return privilegios;
    }

    // ========================================
    // OBTENER PRIVILEGIOS DE UN USUARIO
    // ========================================
    // Este método:
    // 1. Obtiene roles del usuario
    // 2. Busca privilegios de cada rol
    // 3. Une todos los privilegios
    public List<Privilegio> obtenerPrivilegiosUsuario(
            Long usuarioId
    ) {

        // Obtener roles desde microservicio roles
        List<UsuarioRolDTO> rolesUsuario =
                rolClient.obtenerRolesUsuario(
                        usuarioId
                );

        // Lista final de privilegios
        List<Privilegio> privilegios =
                new ArrayList<>();

        // Recorrer roles del usuario
        for (UsuarioRolDTO usuarioRol :
                rolesUsuario) {

            // Obtener privilegios del rol
            List<Privilegio> privilegiosRol =
                    obtenerPrivilegiosRol(
                            usuarioRol.getRolId()
                    );

            // Agregar privilegios encontrados
            privilegios.addAll(privilegiosRol);
        }

        return privilegios;
    }

    // ========================================
    // ELIMINAR PRIVILEGIO
    // ========================================
    public void eliminar(Long id) {

        privilegioRepository.deleteById(id);
    }
}