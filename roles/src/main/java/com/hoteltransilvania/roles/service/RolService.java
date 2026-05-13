package com.hoteltransilvania.roles.service;

import com.hoteltransilvania.roles.DTO.AsignarRolRequest;
import com.hoteltransilvania.roles.models.Rol;
import com.hoteltransilvania.roles.repository.RolRepository;

import com.hoteltransilvania.roles.repository.UsuarioRolRepository;
import com.hoteltransilvania.roles.models.UsuarioRol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {
    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(RolService.class);

    private final RolRepository repository;

    public RolService(RolRepository repository) {
        this.repository = repository;
    }
    public List<UsuarioRol> obtenerRolesUsuario(Long usuarioId) {

    return usuarioRolRepository.findByUsuarioId(usuarioId);
}
    

    // LISTAR
    public List<Rol> listar() {

        logger.info("Listando todos los roles");

        return repository.findAll();
    }

    // OBTENER POR ID
    public Rol obtenerPorId(Long id) {

        logger.info("Buscando rol con ID: {}", id);

        return repository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Rol no encontrado con ID: {}", id);

                    return new RuntimeException("Rol no encontrado");
                });
    }

    // GUARDAR
    public Rol guardar(Rol rol) {

        logger.info("Intentando guardar rol: {}", rol.getNombre());

        repository.findByNombre(rol.getNombre())
                .ifPresent(r -> {

                    logger.warn("El rol ya existe: {}", rol.getNombre());

                    throw new RuntimeException("El rol ya existe");
                });

        Rol rolGuardado = repository.save(rol);

        logger.info("Rol guardado correctamente con ID: {}",
                rolGuardado.getId());

        return rolGuardado;
    }

    // ACTUALIZAR
    public Rol actualizar(Long id, Rol rol) {

        logger.info("Actualizando rol con ID: {}", id);

        Rol existente = obtenerPorId(id);

        existente.setNombre(rol.getNombre());

        Rol rolActualizado = repository.save(existente);

        logger.info("Rol actualizado correctamente con ID: {}",
                rolActualizado.getId());

        return rolActualizado;
    }

    // ELIMINAR
    public void eliminar(Long id) {

        logger.warn("Eliminando rol con ID: {}", id);

        Rol rol = obtenerPorId(id);

        repository.delete(rol);

        logger.info("Rol eliminado correctamente con ID: {}", id);
    }
    public UsuarioRol asignarRol(
        AsignarRolRequest request
) {

    UsuarioRol usuarioRol = UsuarioRol.builder()
            .usuarioId(request.getUsuarioId())
            .rolId(request.getRolId())
            .build();

    return usuarioRolRepository.save(usuarioRol);
}
}