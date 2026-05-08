package com.hoteltransilvania.habitacionesservice.service;

import com.hoteltransilvania.habitacionesservice.entity.Habitacion;
import com.hoteltransilvania.habitacionesservice.repository.HabitacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;

    public HabitacionService(HabitacionRepository habitacionRepository) {
        this.habitacionRepository = habitacionRepository;
    }

    public List<Habitacion> listar() {
        return habitacionRepository.findAll();
    }

    public Habitacion guardar(Habitacion habitacion) {
        return habitacionRepository.save(habitacion);
    }

    public Habitacion obtenerPorId(Long id) {
        return habitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada con ID: " + id));
    }

    public void eliminar(Long id) {
        Habitacion existente = habitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada con ID: " + id));

        habitacionRepository.delete(existente);
    }

    public Habitacion actualizar(Long id, Habitacion habitacion) {
        Habitacion existente = habitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada con ID: " + id));

        existente.setNumero(habitacion.getNumero());
        existente.setTipo(habitacion.getTipo());
        existente.setPrecioPorNoche(habitacion.getPrecioPorNoche());
        existente.setDisponible(habitacion.isDisponible());

        return habitacionRepository.save(existente);
    }
}