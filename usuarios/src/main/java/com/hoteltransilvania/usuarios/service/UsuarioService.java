package com.hoteltransilvania.usuarios.service;

import com.hoteltransilvania.usuarios.DTO.LoginDataDTO;
import com.hoteltransilvania.usuarios.DTO.PrivilegioDTO;
import com.hoteltransilvania.usuarios.models.Usuario;
import com.hoteltransilvania.usuarios.repository.UsuarioRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger =
            LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final RestClient restClient;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RestClient restClient
    ) {
        this.usuarioRepository = usuarioRepository;
        this.restClient = restClient;
    }

    // =========================
    // LISTAR TODOS LOS USUARIOS
    // =========================
    public List<Usuario> listar() {

        logger.info("Listando todos los usuarios");

        return usuarioRepository.findAll();
    }

    // =========================
    // CREAR USUARIO
    // =========================
    public Usuario guardar(
            Usuario usuario
    ) {

        logger.info(
                "Intentando guardar usuario: {}",
                usuario.getUsername()
        );

        // Validar que no exista otro usuario con el mismo username
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {

            logger.warn(
                    "Ya existe un usuario con username: {}",
                    usuario.getUsername()
            );

            throw new RuntimeException(
                    "Existe un usuario con ese username"
            );
        }

        // Guardar usuario en la base de datos
        Usuario usuarioGuardado =
                usuarioRepository.save(usuario);

        logger.info(
                "Usuario guardado correctamente con ID: {}",
                usuarioGuardado.getId()
        );

        return usuarioGuardado;
    }

    // =========================
    // LOGIN DE USUARIO
    // =========================
    // Este método:
    // 1. Busca el usuario por username
    // 2. Valida la contraseña
    // 3. Consulta los privilegios heredados del usuario
    // 4. Devuelve el usuario junto a sus privilegios
    public LoginDataDTO login(
            String username,
            String password
    ) {

        logger.info(
                "Intento de login para usuario: {}",
                username
        );

        // Buscar usuario por username
        Usuario usuario =
                usuarioRepository.findByUsername(username)
                        .orElseThrow(() -> {

                            logger.error(
                                    "Usuario no encontrado: {}",
                                    username
                            );

                            return new RuntimeException(
                                    "Usuario no encontrado"
                            );
                        });

        // Validar contraseña
        if (!usuario.getPassword().equals(password)) {

            logger.warn(
                    "Contraseña incorrecta para usuario: {}",
                    username
            );

            throw new RuntimeException(
                    "Contraseña incorrecta"
            );
        }

        logger.info(
                "Login exitoso para usuario: {}",
                username
        );

        // Consultar privilegios heredados del usuario
        // Esta ruta llama al microservicio privilegios
        // y obtiene los privilegios según los roles asignados al usuario
        PrivilegioDTO[] privilegios =
                restClient.get()
                        .uri(
                                "http://privilegios-service:8093/privilegios/usuarios/"
                                        + usuario.getId()
                        )
                        .retrieve()
                        .body(PrivilegioDTO[].class);

        logger.info(
                "Privilegios obtenidos: {}",
                Arrays.toString(privilegios)
        );

        // Si no hay privilegios, retorna lista vacía
        return new LoginDataDTO(
                usuario,
                privilegios == null
                        ? List.of()
                        : List.of(privilegios)
        );
    }

    // =========================
    // OBTENER USUARIO POR ID
    // =========================
    public Usuario obtenerPorId(
            Long id
    ) {

        logger.info(
                "Buscando usuario con ID: {}",
                id
        );

        return usuarioRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error(
                            "Usuario no encontrado con ID: {}",
                            id
                    );

                    return new RuntimeException(
                            "Usuario no encontrado con ID: " + id
                    );
                });
    }

    // =========================
    // ACTUALIZAR USUARIO
    // =========================
    public Usuario actualizar(
            Long id,
            Usuario usuario
    ) {

        logger.info(
                "Actualizando usuario con ID: {}",
                id
        );

        // Buscar usuario existente
        Usuario existente =
                obtenerPorId(id);

        // Actualizar campos
        existente.setUsername(usuario.getUsername());
        existente.setPassword(usuario.getPassword());
        existente.setRolId(usuario.getRolId());

        // Guardar cambios
        Usuario usuarioActualizado =
                usuarioRepository.save(existente);

        logger.info(
                "Usuario actualizado correctamente con ID: {}",
                usuarioActualizado.getId()
        );

        return usuarioActualizado;
    }

    // =========================
    // ELIMINAR USUARIO
    // =========================
    public void eliminar(
            Long id
    ) {

        logger.warn(
                "Eliminando usuario con ID: {}",
                id
        );

        // Buscar usuario antes de eliminar
        Usuario existente =
                obtenerPorId(id);

        // Eliminar usuario
        usuarioRepository.delete(existente);

        logger.info(
                "Usuario eliminado correctamente con ID: {}",
                id
        );
    }
}