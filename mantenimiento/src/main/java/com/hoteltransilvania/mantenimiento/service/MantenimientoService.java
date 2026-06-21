package com.hoteltransilvania.mantenimiento.service;

import com.hoteltransilvania.mantenimiento.client.HabitacionClient;
import com.hoteltransilvania.mantenimiento.dto.EstadoHabitacionDTO;
import com.hoteltransilvania.mantenimiento.dto.MantenimientoDTO;
import com.hoteltransilvania.mantenimiento.exception.ResourceNotFoundException;
import com.hoteltransilvania.mantenimiento.model.MantenimientoModel;
import com.hoteltransilvania.mantenimiento.repository.MantenimientoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;
    private final HabitacionClient habitacionClient;

    public MantenimientoService(
            MantenimientoRepository mantenimientoRepository,
            HabitacionClient habitacionClient) {

        this.mantenimientoRepository = mantenimientoRepository;
        this.habitacionClient = habitacionClient;
    }

    public MantenimientoModel guardar(MantenimientoDTO dto) {

        log.info(
                "Iniciando registro de mantenimiento para habitación ID: {}",
                dto.getIdHabitacion());

        LocalDate hoy = LocalDate.now();

        if (dto.getFechaInicio().isBefore(hoy)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La fecha de inicio no puede ser anterior a hoy");
        }

        if (!dto.getFechaFin().isAfter(dto.getFechaInicio())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La fecha fin debe ser posterior a la fecha inicio");
        }

        MantenimientoModel mantenimiento = new MantenimientoModel();

        mantenimiento.setIdHabitacion(dto.getIdHabitacion());
        mantenimiento.setDescripcion(dto.getDescripcion());
        mantenimiento.setFechaInicio(dto.getFechaInicio());
        mantenimiento.setFechaFin(dto.getFechaFin());
        mantenimiento.setEstado(dto.getEstado());

        MantenimientoModel guardado =
                mantenimientoRepository.save(mantenimiento);

        if ("EN_PROCESO".equals(dto.getEstado())) {

                EstadoHabitacionDTO estadoHabitacion =
                        new EstadoHabitacionDTO();

                estadoHabitacion.setDisponible(false);

                habitacionClient.actualizarEstadoHabitacion(
                        dto.getIdHabitacion(),
                        estadoHabitacion);

                log.info(
                        "Habitación {} marcada como NO DISPONIBLE",
                        dto.getIdHabitacion());
        }

        if ("FINALIZADO".equals(dto.getEstado())) {

                EstadoHabitacionDTO estadoHabitacion =
                        new EstadoHabitacionDTO();

                estadoHabitacion.setDisponible(true);

                habitacionClient.actualizarEstadoHabitacion(
                        dto.getIdHabitacion(),
                        estadoHabitacion);

                log.info(
                        "Habitación {} marcada como DISPONIBLE",
                        dto.getIdHabitacion());
        }

        log.info(
                "Mantenimiento registrado correctamente ID: {}",
                guardado.getId());

        return guardado;
    }

    public List<MantenimientoModel> listarTodos() {

        log.info("Listando todos los mantenimientos");

        return mantenimientoRepository.findAll();
    }

    public MantenimientoModel buscarPorId(Long id) {

        log.info(
                "Buscando mantenimiento ID: {}",
                id);

        return mantenimientoRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Mantenimiento no encontrado ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Mantenimiento no encontrado con ID: "
                            + id);
                });
    }

    public void eliminar(Long id) {

        log.warn(
                "Eliminando mantenimiento ID: {}",
                id);

        MantenimientoModel mantenimiento =
                buscarPorId(id);

        mantenimientoRepository.delete(mantenimiento);

        log.info(
                "Mantenimiento eliminado correctamente ID: {}",
                id);
    }

   public MantenimientoModel modificarEstado(
                Long id,
                String estado) {

        log.info(
                "Modificando estado de mantenimiento ID: {} a estado: {}",
                id,
                estado);

        MantenimientoModel mantenimiento =
                mantenimientoRepository.findById(id)
                        .orElseThrow(() -> {

                                log.warn(
                                        "Mantenimiento no encontrado para modificar estado ID: {}",
                                        id);

                                return new ResourceNotFoundException(
                                        "Mantenimiento no encontrado con ID: " + id);
                        });

        if (!estado.equals("PENDIENTE") &&
                !estado.equals("EN_PROCESO") &&
                !estado.equals("FINALIZADO")) {

                log.warn(
                        "Estado inválido recibido para modificación: {}",
                        estado);

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Estado inválido");
        }

        mantenimiento.setEstado(estado);

        if (estado.equals("EN_PROCESO")) {

                EstadoHabitacionDTO estadoHabitacion =
                        new EstadoHabitacionDTO();

                estadoHabitacion.setDisponible(false);

                habitacionClient.actualizarEstadoHabitacion(
                        mantenimiento.getIdHabitacion(),
                        estadoHabitacion);
        }

        if (estado.equals("FINALIZADO")) {

                EstadoHabitacionDTO estadoHabitacion =
                        new EstadoHabitacionDTO();

                estadoHabitacion.setDisponible(true);

                habitacionClient.actualizarEstadoHabitacion(
                        mantenimiento.getIdHabitacion(),
                        estadoHabitacion);
        }

        MantenimientoModel actualizado =
                mantenimientoRepository.save(mantenimiento);

        log.info(
                "Estado de mantenimiento actualizado correctamente ID: {}",
                id);

        return actualizado;
    }

    public MantenimientoModel actualizar(
            Long id,
            MantenimientoDTO dto) {

        log.info(
                "Actualizando mantenimiento ID: {}",
                id);

        MantenimientoModel mantenimiento =
                mantenimientoRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Mantenimiento no encontrado para actualización ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Mantenimiento no encontrado con ID: "
                            + id);
                });

        mantenimiento.setIdHabitacion(dto.getIdHabitacion());
        mantenimiento.setDescripcion(dto.getDescripcion());
        mantenimiento.setFechaInicio(dto.getFechaInicio());
        mantenimiento.setFechaFin(dto.getFechaFin());
        mantenimiento.setEstado(dto.getEstado());

        MantenimientoModel actualizado =
                mantenimientoRepository.save(mantenimiento);

        log.info(
                "Mantenimiento actualizado correctamente ID: {}",
                id);

        return actualizado;
    }
}