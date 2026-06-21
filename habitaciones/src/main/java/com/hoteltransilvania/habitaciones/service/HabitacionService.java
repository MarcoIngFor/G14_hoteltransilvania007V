package com.hoteltransilvania.habitaciones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoteltransilvania.habitaciones.Model.HabitacionModel;
import com.hoteltransilvania.habitaciones.dto.HabitacionDTO;
import com.hoteltransilvania.habitaciones.dto.RespuestaDTO.HabitacionRespuestaDTO;
import com.hoteltransilvania.habitaciones.exception.DuplicateResourceException;
import com.hoteltransilvania.habitaciones.exception.ResourceNotFoundException;
import com.hoteltransilvania.habitaciones.repository.HabitacionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

        public HabitacionService(HabitacionRepository habitacionRepository) {
                this.habitacionRepository = habitacionRepository;
        }

    public HabitacionRespuestaDTO guardar(HabitacionDTO dto){

        log.info(
                "Iniciando registro de habitación número: {}",
                dto.getNumero());

        if (habitacionRepository.existsByNumero(dto.getNumero())) {

            log.warn(
                    "Intento de registro duplicado habitación número: {}",
                    dto.getNumero());

            throw new DuplicateResourceException(
                    "La habitación "
                    + dto.getNumero()
                    + " ya está registrada.");
        }

        HabitacionModel habitacion = new HabitacionModel();

        habitacion.setNumero(dto.getNumero());
        habitacion.setPrecioPorNoche(dto.getPrecioPorNoche());
        habitacion.setTipo(dto.getTipo());
        habitacion.setDisponible(dto.isDisponible());

        HabitacionModel habitacionGuardada =
                habitacionRepository.save(habitacion);

        log.info(
                "Habitación registrada correctamente ID: {}",
                habitacionGuardada.getId());

        HabitacionRespuestaDTO respuesta =
                new HabitacionRespuestaDTO();

        respuesta.setMensaje(
                "La habitación ha sido generada y guardada con éxito");

        return respuesta;
    }

    public List<HabitacionModel> listarTodos(){

        log.info("Listando todas las habitaciones");

        return habitacionRepository.findAll();
    }

    public HabitacionModel buscarPorId(Long id) {

        log.info(
                "Buscando habitación ID: {}",
                id);

        return habitacionRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Habitación no encontrada ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Habitación no encontrada con ID: "
                            + id);
                });
    }

    public void actualizarDisponibilidad(
            Long id,
            boolean disponible) {

        log.info(
                "Actualizando disponibilidad habitación ID: {}",
                id);

        HabitacionModel hab =
                habitacionRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Habitación no encontrada para actualizar estado ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Habitación no encontrada");
                });

        hab.setDisponible(disponible);

        habitacionRepository.save(hab);

        log.info(
                "Disponibilidad actualizada habitación ID: {}",
                id);
    }

    public HabitacionModel actualizar(
            Long id,
            HabitacionDTO dto) {

        log.info(
                "Actualizando habitación ID: {}",
                id);

        HabitacionModel habitacion =
                habitacionRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Habitación no encontrada para actualización ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Habitación no encontrada con ID: "+ id);
                });

        habitacion.setNumero(dto.getNumero());
        habitacion.setTipo(dto.getTipo());
        habitacion.setPrecioPorNoche(dto.getPrecioPorNoche());
        habitacion.setDisponible(dto.isDisponible());

        HabitacionModel actualizada =
                habitacionRepository.save(habitacion);

        log.info(
                "Habitación actualizada correctamente ID: {}",
                id);

        return actualizada;
    }

    public void eliminar(Long id){

        log.warn(
                "Eliminando habitación ID: {}",
                id);

        HabitacionModel habitacion =
                habitacionRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Habitación no encontrada para eliminar ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Habitación no encontrada con ID: "
                            + id);
                });

        habitacionRepository.delete(habitacion);

        log.info(
                "Habitación eliminada correctamente ID: {}",
                id);
    }
}