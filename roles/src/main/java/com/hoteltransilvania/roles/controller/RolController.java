package com.hoteltransilvania.roles.controller;

import com.hoteltransilvania.roles.DTO.AsignarRolRequest;
import com.hoteltransilvania.roles.models.Rol;
import com.hoteltransilvania.roles.service.RolService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RolController {

    private static final Logger logger =
            LoggerFactory.getLogger(RolController.class);

    private final RolService service;

    public RolController(RolService service) {
        this.service = service;
    }

    // =========================
    // LISTAR TODOS LOS ROLES
    // =========================
    @GetMapping
    public ResponseEntity<?> listar() {

        logger.info("GET /roles - Listando todos los roles");

        return new ResponseEntity<>(
                service.listar(),
                HttpStatus.OK
        );
    }

    // =========================
    // OBTENER ROL POR ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(
            @PathVariable Long id
    ) {

        logger.info(
                "GET /roles/{} - Buscando rol por ID",
                id
        );

        return new ResponseEntity<>(
                service.obtenerPorId(id),
                HttpStatus.OK
        );
    }

    // =========================
    // CREAR ROL
    // =========================
    @PostMapping
    public ResponseEntity<?> guardar(
            @RequestBody Rol rol
    ) {

        logger.info(
                "POST /roles - Creando rol: {}",
                rol.getNombre()
        );

        Rol nuevoRol = service.guardar(rol);

        logger.info(
                "Rol creado correctamente con ID: {}",
                nuevoRol.getId()
        );

        return new ResponseEntity<>(
                nuevoRol,
                HttpStatus.CREATED
        );
    }

    // =========================
    // ASIGNAR ROL A USUARIO
    // =========================
    @PostMapping("/asignar")
    public ResponseEntity<?> asignarRol(
            @RequestBody AsignarRolRequest request
    ) {

        logger.info(
                "POST /roles/asignar - usuarioId: {}, rolId: {}",
                request.getUsuarioId(),
                request.getRolId()
        );

        return ResponseEntity.ok(
                service.asignarRol(request)
        );
    }

    // =========================
    // OBTENER ROLES DE UN USUARIO
    // =========================
    @GetMapping("/usuarios/{usuarioId}")
    public ResponseEntity<?> obtenerRolesUsuario(
            @PathVariable Long usuarioId
    ) {

        logger.info(
                "GET /roles/usuarios/{}",
                usuarioId
        );

        return ResponseEntity.ok(
                service.obtenerRolesUsuario(usuarioId)
        );
    }

    // =========================
    // ACTUALIZAR ROL
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody Rol rol
    ) {

        logger.info(
                "PUT /roles/{} - Actualizando rol",
                id
        );

        Rol rolActualizado = service.actualizar(id, rol);

        logger.info(
                "Rol actualizado correctamente con ID: {}",
                rolActualizado.getId()
        );

        return new ResponseEntity<>(
                rolActualizado,
                HttpStatus.OK
        );
    }

    // =========================
    // ELIMINAR ROL
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable Long id
    ) {

        logger.warn(
                "DELETE /roles/{} - Eliminando rol",
                id
        );

        service.eliminar(id);

        logger.info(
                "Rol eliminado correctamente con ID: {}",
                id
        );

        return new ResponseEntity<>(
                "Rol eliminado correctamente",
                HttpStatus.OK
        );
    }
}