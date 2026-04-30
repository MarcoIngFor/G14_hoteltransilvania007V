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
        return habitacionRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        habitacionRepository.deleteById(id);
    }

    public Habitacion actualizar(Long id, Habitacion habitacion) {
        Habitacion existente = habitacionRepository.findById(id).orElse(null);

        if (existente == null) {
            return null;
        }

        existente.setNumero(habitacion.getNumero());
        existente.setTipo(habitacion.getTipo());
        existente.setPrecioPorNoche(habitacion.getPrecioPorNoche());
        existente.setDisponible(habitacion.isDisponible());

        return habitacionRepository.save(existente);
    }
}