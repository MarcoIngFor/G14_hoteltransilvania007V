package com.hoteltransilvania.habitaciones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoteltransilvania.habitaciones.Model.HabitacionModel;
import com.hoteltransilvania.habitaciones.dto.HabitacionDTO;
import com.hoteltransilvania.habitaciones.dto.RespuestaDTO.HabitacionRespuestaDTO;
import com.hoteltransilvania.habitaciones.exception.DuplicateResourceException;
import com.hoteltransilvania.habitaciones.repository.HabitacionRepository;

@Service

public class HabitacionService {

    @Autowired//Instancio a repository
    private HabitacionRepository habitacionRepository;
    
    public HabitacionRespuestaDTO guardar (HabitacionDTO dto){
        
        if (habitacionRepository.existsByNumero(dto.getNumero())) {
        throw new DuplicateResourceException("La habitación " + dto.getNumero() + " ya está registrada.");
        }
        HabitacionModel habitacion = new HabitacionModel();
        habitacion.setNumero(dto.getNumero());
        habitacion.setPrecioPorNoche(dto.getPrecioPorNoche());
        habitacion.setTipo(dto.getTipo());
        habitacion.setDisponible(dto.isDisponible());

        habitacionRepository.save(habitacion);

        HabitacionRespuestaDTO respuesta = new HabitacionRespuestaDTO();
        respuesta.setMensaje("La habitacion a sido generada y guardada con exito");

        return respuesta;
    }

    public List<HabitacionModel> listarTodos(){
        return habitacionRepository.findAll();
    }
    
    public HabitacionModel buscarPorId(Long id) {
        return habitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }

    public void actualizarDisponibilidad(Long id, boolean disponible) {
        HabitacionModel hab = habitacionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));
        hab.setDisponible(disponible);
        habitacionRepository.save(hab);
    }
}
