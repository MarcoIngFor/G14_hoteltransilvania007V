package com.hoteltransilvania.privilegios.controller;

import com.hoteltransilvania.privilegios.DTO.AsignarPrivilegioRequest;
import com.hoteltransilvania.privilegios.models.Privilegio;
import com.hoteltransilvania.privilegios.service.PrivilegioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/privilegios")
public class PrivilegioController {

    private static final Logger logger =
            LoggerFactory.getLogger(PrivilegioController.class);

    private final PrivilegioService service;

    public PrivilegioController(PrivilegioService service) {
        this.service = service;
    }

    // =========================
    // LISTAR TODOS LOS PRIVILEGIOS
    // =========================
    @GetMapping
    public ResponseEntity<?> listar() {

        logger.info("GET /privilegios - Listando privilegios");

        return new ResponseEntity<>(
                service.listar(),
                HttpStatus.OK
        );
    }

    // =========================
    // OBTENER PRIVILEGIO POR ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(
            @PathVariable Long id
    ) {

        logger.info(
                "GET /privilegios/{} - Buscando privilegio por ID",
                id
        );

        return new ResponseEntity<>(
                service.obtenerPorId(id),
                HttpStatus.OK
        );
    }

    // =========================
    // CREAR PRIVILEGIO
    // =========================
    @PostMapping
    public ResponseEntity<?> guardar(
            @RequestBody Privilegio privilegio
    ) {

        logger.info(
                "POST /privilegios - Creando privilegio: {}",
                privilegio.getNombre()
        );

        return new ResponseEntity<>(
                service.guardar(privilegio),
                HttpStatus.CREATED
        );
    }

    // =========================
    // ASIGNAR PRIVILEGIO A ROL
    // =========================
    @PostMapping("/asignar")
    public ResponseEntity<?> asignarPrivilegio(
            @RequestBody AsignarPrivilegioRequest request
    ) {

        logger.info(
                "POST /privilegios/asignar - rolId: {}, privilegioId: {}",
                request.getRolId(),
                request.getPrivilegioId()
        );

        return new ResponseEntity<>(
                service.asignarPrivilegio(request),
                HttpStatus.OK
        );
    }

    // =========================
    // VER PRIVILEGIOS DE UN ROL
    // =========================
    @GetMapping("/rol/{rolId}")
    public ResponseEntity<?> obtenerPrivilegiosRol(
            @PathVariable Long rolId
    ) {

        logger.info(
                "GET /privilegios/rol/{} - Buscando privilegios del rol",
                rolId
        );

        return new ResponseEntity<>(
                service.obtenerPrivilegiosRol(rolId),
                HttpStatus.OK
        );
    }

    // =========================
    // VER PRIVILEGIOS DE UN USUARIO
    // =========================
    // Este endpoint:
    // 1. Recibe el ID del usuario
    // 2. Busca los roles del usuario
    // 3. Busca los privilegios de esos roles
    // 4. Devuelve todos los privilegios heredados
    @GetMapping("/usuarios/{usuarioId}")
    public ResponseEntity<?> obtenerPrivilegiosUsuario(
            @PathVariable Long usuarioId
    ) {

        logger.info(
                "GET /privilegios/usuarios/{} - Buscando privilegios del usuario",
                usuarioId
        );

        return new ResponseEntity<>(
                service.obtenerPrivilegiosUsuario(usuarioId),
                HttpStatus.OK
        );
    }

    // =========================
    // ELIMINAR PRIVILEGIO
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable Long id
    ) {

        logger.warn(
                "DELETE /privilegios/{} - Eliminando privilegio",
                id
        );

        service.eliminar(id);

        return new ResponseEntity<>(
                "Privilegio eliminado correctamente",
                HttpStatus.OK
        );
    }
}