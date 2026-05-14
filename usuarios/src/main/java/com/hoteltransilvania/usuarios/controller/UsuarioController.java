package com.hoteltransilvania.usuarios.controller;

import com.hoteltransilvania.usuarios.DTO.LoginDataDTO;
import com.hoteltransilvania.usuarios.DTO.LoginRequest;
import com.hoteltransilvania.usuarios.DTO.LoginResponse;
import com.hoteltransilvania.usuarios.models.Usuario;
import com.hoteltransilvania.usuarios.models.UsuarioSesion;
import com.hoteltransilvania.usuarios.repository.UsuarioSesionRepository;
import com.hoteltransilvania.usuarios.security.JwtUtil;
import com.hoteltransilvania.usuarios.service.UsuarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private static final Logger logger =
            LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService service;
    private final JwtUtil jwtUtil;
    private final UsuarioSesionRepository usuarioSesionRepository;

    public UsuarioController(
            UsuarioService service,
            JwtUtil jwtUtil,
            UsuarioSesionRepository usuarioSesionRepository
    ) {
        this.service = service;
        this.jwtUtil = jwtUtil;
        this.usuarioSesionRepository = usuarioSesionRepository;
    }

    // =========================
    // LISTAR TODOS LOS USUARIOS
    // =========================
    @GetMapping
    public ResponseEntity<?> listar() {

        logger.info("GET /usuarios - Listando todos los usuarios");

        return new ResponseEntity<>(
                service.listar(),
                HttpStatus.OK
        );
    }

    // =========================
    // OBTENER USUARIO POR ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(
            @PathVariable Long id
    ) {

        logger.info(
                "GET /usuarios/{} - Buscando usuario por ID",
                id
        );

        return new ResponseEntity<>(
                service.obtenerPorId(id),
                HttpStatus.OK
        );
    }

    // =========================
    // CREAR USUARIO
    // =========================
    @PostMapping
    public ResponseEntity<?> guardar(
            @RequestBody Usuario usuario
    ) {

        logger.info(
                "POST /usuarios - Creando usuario: {}",
                usuario.getUsername()
        );

        Usuario nuevoUsuario =
                service.guardar(usuario);

        logger.info(
                "Usuario creado correctamente con ID: {}",
                nuevoUsuario.getId()
        );

        return new ResponseEntity<>(
                nuevoUsuario,
                HttpStatus.CREATED
        );
    }

    // =========================
    // LOGIN DE USUARIO
    // =========================
    // Este endpoint:
    // 1. Valida username y password
    // 2. Obtiene roles y privilegios del usuario
    // 3. Genera el token JWT
    // 4. Guarda una sesión activa en la BD
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ) {

        logger.info(
                "POST /usuarios/login - Intento de login para usuario: {}",
                request.getUsername()
        );

        LoginDataDTO loginData =
                service.login(
                        request.getUsername(),
                        request.getPassword()
                );

        Usuario usuario =
                loginData.getUsuario();

        logger.info(
                "Login exitoso para usuario: {}",
                usuario.getUsername()
        );

        String token =
                jwtUtil.generarToken(
                        usuario.getUsername(),
                        usuario.getRolId(),
                        loginData.getPrivilegios()
                );

        // Guardar sesión activa del usuario logeado
        UsuarioSesion sesion =
                UsuarioSesion.builder()
                        .usuarioId(usuario.getId())
                        .username(usuario.getUsername())
                        .fechaLogin(LocalDateTime.now())
                        .activo(true)
                        .build();

        usuarioSesionRepository.save(sesion);

        LoginResponse response =
                new LoginResponse(
                        usuario.getId(),
                        usuario.getUsername(),
                        usuario.getRolId(),
                        token,
                        "Login exitoso"
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    // =========================
    // VER USUARIOS LOGEADOS
    // =========================
    // Muestra todas las sesiones activas
    @GetMapping("/logeados")
    public ResponseEntity<?> usuariosLogeados() {

        logger.info(
                "GET /usuarios/logeados - Listando usuarios con sesión activa"
        );

        return new ResponseEntity<>(
                usuarioSesionRepository.findByActivoTrue(),
                HttpStatus.OK
        );
    }

    // =========================
    // ACTUALIZAR USUARIO
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody Usuario usuario
    ) {

        logger.info(
                "PUT /usuarios/{} - Actualizando usuario",
                id
        );

        Usuario usuarioActualizado =
                service.actualizar(id, usuario);

        logger.info(
                "Usuario actualizado correctamente con ID: {}",
                usuarioActualizado.getId()
        );

        return new ResponseEntity<>(
                usuarioActualizado,
                HttpStatus.OK
        );
    }

    // =========================
    // ELIMINAR USUARIO
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable Long id
    ) {

        logger.warn(
                "DELETE /usuarios/{} - Eliminando usuario",
                id
        );

        service.eliminar(id);

        logger.info(
                "Usuario eliminado correctamente con ID: {}",
                id
        );

        return new ResponseEntity<>(
                "Usuario eliminado correctamente",
                HttpStatus.OK
        );
    }
    
}