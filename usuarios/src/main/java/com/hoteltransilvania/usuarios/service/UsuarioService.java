package com.hoteltransilvania.usuarios.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hoteltransilvania.usuarios.dto.UsuarioDTO;
import com.hoteltransilvania.usuarios.exception.ResourceNotFoundException;
import com.hoteltransilvania.usuarios.model.UsuarioModel;
import com.hoteltransilvania.usuarios.repository.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder passwordEncoder){

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioModel> listar() {

        log.info("Listando todos los usuarios");

        return usuarioRepository.findAll();
    }

    public UsuarioModel buscarPorId(Long id) {

        log.info(
                "Buscando usuario ID: {}",
                id);

        return usuarioRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Usuario no encontrado ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Usuario no encontrado con ID: "
                            + id);
                });
    }

    public UsuarioModel crear(UsuarioModel usuario) {

        log.info(
                "Creando usuario: {}",
                usuario.getUsername());

        usuario.setPassword(
                passwordEncoder.encode(
                        usuario.getPassword()));

        UsuarioModel usuarioGuardado =
                usuarioRepository.save(usuario);

        log.info(
                "Usuario registrado correctamente ID: {}",
                usuarioGuardado.getId());

        return usuarioGuardado;
    }

    public UsuarioModel login(
            String username,
            String password) {

        log.info(
                "Intento de login usuario: {}",
                username);

        UsuarioModel usuario =
                usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {

                    log.warn(
                            "Usuario no encontrado para login: {}",
                            username);

                    return new ResourceNotFoundException(
                            "Usuario no encontrado");
                });

        if (!usuario.isActivo()) {

            log.warn(
                    "Intento de acceso usuario inactivo: {}",
                    username);

            throw new ResourceNotFoundException(
                    "Usuario inactivo");
        }

        if (!passwordEncoder.matches(
                password,
                usuario.getPassword())) {

            log.warn(
                    "Contraseña incorrecta usuario: {}",
                    username);

            throw new ResourceNotFoundException(
                    "Contraseña incorrecta");
        }

        log.info(
                "Login exitoso usuario: {}",
                username);

        return usuario;
    }

    public UsuarioModel actualizar(
            Long id,
            UsuarioDTO dto) {

        log.info(
                "Actualizando usuario ID: {}",
                id);

        UsuarioModel usuario =
                usuarioRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Usuario no encontrado para actualización ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Usuario no encontrado con ID: "
                            + id);
                });

        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());

        usuario.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()));

        UsuarioModel actualizado =
                usuarioRepository.save(usuario);

        log.info(
                "Usuario actualizado correctamente ID: {}",
                id);

        return actualizado;
    }

    public void eliminar(Long id) {

        log.warn(
                "Eliminando usuario ID: {}",
                id);

        UsuarioModel usuario =
                usuarioRepository.findById(id)
                .orElseThrow(() -> {log.warn("Usuario no encontrado para eliminar ID: {}",id);

                    return new ResourceNotFoundException("Usuario no encontrado con ID: "+ id);
                });

        usuarioRepository.delete(usuario);

        log.info(
                "Usuario eliminado correctamente ID: {}",
                id);
    }
}