package com.hoteltransilvania.usuarios.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hoteltransilvania.usuarios.dto.LoginDTO;
import com.hoteltransilvania.usuarios.dto.UsuarioDTO;
import com.hoteltransilvania.usuarios.dto.RespuestaDTO.EliminarRespuestaDTO;
import com.hoteltransilvania.usuarios.dto.RespuestaDTO.LoginRespuestaDTO;
import com.hoteltransilvania.usuarios.dto.RespuestaDTO.UsuarioRespuestaDTO;
import com.hoteltransilvania.usuarios.model.UsuarioModel;
import com.hoteltransilvania.usuarios.service.UsuarioService;
import com.hoteltransilvania.usuarios.service.security.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Usuarios", description = "Módulo de gestión de usuarios del sistema")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public UsuarioController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @Operation(
            summary = "Listar usuarios",
            description = "Retorna todos los usuarios registrados en el sistema. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuarios listados correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping
    public ResponseEntity<List<UsuarioModel>> listarTodo() {
        List<UsuarioModel> lista = usuarioService.listar();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(
            summary = "Obtener usuario por ID",
            description = "Retorna la información completa de un usuario registrado según el ID proporcionado. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder al recurso"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> listarPorId(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {

        UsuarioModel usuario = usuarioService.buscarPorId(id);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @Operation(
            summary = "Crear usuario",
            description = "Crea un nuevo usuario en el sistema. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para crear usuarios"),
            @ApiResponse(responseCode = "409", description = "Conflicto: correo o usuario ya registrado")
    })
    @PostMapping
    public ResponseEntity<UsuarioRespuestaDTO> crear(@Valid @RequestBody UsuarioDTO dto) {

        UsuarioModel user = new UsuarioModel();

        user.setNombre(dto.getNombre());
        user.setApellido(dto.getApellido());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setCorreo(dto.getCorreo());
        user.setRol(dto.getRol());
        user.setActivo(true);

        UsuarioModel newUser = usuarioService.crear(user);

        UsuarioRespuestaDTO respuesta = new UsuarioRespuestaDTO();

        respuesta.setNombre(newUser.getNombre());
        respuesta.setApellido(newUser.getApellido());
        respuesta.setMensaje("El Usuario "
                + newUser.getNombre() + " "
                + newUser.getApellido()
                + " ha sido creado de forma exitosa");

        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Valida las credenciales del usuario y retorna un token JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginRespuestaDTO> login(@Valid @RequestBody LoginDTO dto) {

        UsuarioModel usuario = usuarioService.login(dto.getUsername(), dto.getPassword());

        String token = jwtService.generateToken(usuario.getUsername(), usuario.getRol());

        LoginRespuestaDTO respuesta = new LoginRespuestaDTO();

        respuesta.setRol(usuario.getRol());
        respuesta.setMensaje("Login exitoso. Bienvenido "
                + usuario.getNombre() + " "
                + usuario.getApellido());
        respuesta.setToken(token);

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza la información de un usuario existente según su ID. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar usuarios"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto: correo o usuario ya registrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioRespuestaDTO> actualizar(
            @Parameter(description = "ID del usuario que se desea actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO dto) {

        UsuarioModel usuarioActualizado = usuarioService.actualizar(id, dto);

        UsuarioRespuestaDTO respuesta = new UsuarioRespuestaDTO();

        respuesta.setNombre(usuarioActualizado.getNombre());
        respuesta.setApellido(usuarioActualizado.getApellido());
        respuesta.setMensaje("El Usuario "
                + usuarioActualizado.getNombre() + " "
                + usuarioActualizado.getApellido()
                + " ha sido modificado de forma exitosa");

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario del sistema según su ID. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar usuarios"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<EliminarRespuestaDTO> eliminar(
            @Parameter(description = "ID del usuario que se desea eliminar", example = "1")
            @PathVariable Long id) {

        usuarioService.eliminar(id);

        EliminarRespuestaDTO respuesta = new EliminarRespuestaDTO();

        respuesta.setMensaje("Usuario con id: " + id + ", ha sido eliminado");

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}