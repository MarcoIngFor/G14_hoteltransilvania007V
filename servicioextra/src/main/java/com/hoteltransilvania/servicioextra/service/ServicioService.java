package com.hoteltransilvania.servicioextra.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.hoteltransilvania.servicioextra.entity.Servicio;
import com.hoteltransilvania.servicioextra.repository.ServicioRepository;

@Service

public class ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository){
        this.servicioRepository = servicioRepository;
    }

    public List<Servicio> listar() {
        return servicioRepository.findAll();
    }

    public Servicio guardar(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    public Servicio obtenerPorId(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));
    }

    public void eliminar(Long id) {
        Servicio existente = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));

        servicioRepository.delete(existente);
    }

    public Servicio actualizar(Long id, Servicio servicio) {
        Servicio existente = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));

        existente.setNombre(servicio.getNombre());
        existente.setDescripcion(servicio.getDescripcion());
        existente.setPrecio(servicio.getPrecio());
        existente.setActivo(servicio.isActivo());

        return servicioRepository.save(existente);
    }

}
