package com.hoteltransilvania.roles.service;

import com.hoteltransilvania.roles.models.Rol;
import com.hoteltransilvania.roles.repository.RolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    private final RolRepository repository;

    public RolService(RolRepository repository) {
        this.repository = repository;
    }

    // LISTAR
    public List<Rol> listar() {
        return repository.findAll();
    }

    // OBTENER POR ID
    public Rol obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Rol no encontrado"));
    }

    // GUARDAR
    public Rol guardar(Rol rol) {

        repository.findByNombre(rol.getNombre())
                .ifPresent(r -> {
                    throw new RuntimeException("El rol ya existe");
                });

        return repository.save(rol);
    }

    // ACTUALIZAR
    public Rol actualizar(Long id, Rol rol) {

        Rol existente = obtenerPorId(id);

        existente.setNombre(rol.getNombre());

        return repository.save(existente);
    }

    // ELIMINAR
    public void eliminar(Long id) {

        Rol rol = obtenerPorId(id);

        repository.delete(rol);
    }
}