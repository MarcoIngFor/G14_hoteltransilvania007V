package com.hoteltransilvania.usuarios.service;

import com.hoteltransilvania.usuarios.models.Usuario;
import com.hoteltransilvania.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;

@Service
public class UsuarioService {
    @Value("${roles-service.url}")
    private String rolesServiceUrl;

    @Value("${login-service.url}")
    
    private String loginServiceUrl;

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario guardar(Usuario usuario) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("existe un usuario con ese username");
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario login(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return usuario;
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario existente = obtenerPorId(id);

        existente.setUsername(usuario.getUsername());
        existente.setPassword(usuario.getPassword());
        existente.setRolId(usuario.getRolId());

        return usuarioRepository.save(existente);
    }

    public void eliminar(Long id) {
        Usuario existente = obtenerPorId(id);
        usuarioRepository.delete(existente);
    }
}