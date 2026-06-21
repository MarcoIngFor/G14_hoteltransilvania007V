package com.hoteltransilvania.reservas.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.hoteltransilvania.reservas.Model.ReservaModel;
import com.hoteltransilvania.reservas.client.ClienteClient;
import com.hoteltransilvania.reservas.client.HabitacionClient;
import com.hoteltransilvania.reservas.client.NotificacionClient;
import com.hoteltransilvania.reservas.dto.ClienteDTO;
import com.hoteltransilvania.reservas.dto.HabitacionDTO;
import com.hoteltransilvania.reservas.dto.NotificacionDTO;
import com.hoteltransilvania.reservas.dto.ReservaDTO;
import com.hoteltransilvania.reservas.dto.RespuestaDTO.ReservaRespuestaDTO;
import com.hoteltransilvania.reservas.exception.ResourceNotFoundException;
import com.hoteltransilvania.reservas.repository.ReservaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReservaServices {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClienteClient clienteClient;

    @Autowired
    private HabitacionClient habitacionClient;

    @Autowired
    private NotificacionClient notificacionClient;

    public ReservaServices(ReservaRepository reservaRepository, ClienteClient clienteClient, HabitacionClient habitacionClient, NotificacionClient notificacionClient){
        this.reservaRepository = reservaRepository;
        this.clienteClient = clienteClient;
        this.habitacionClient = habitacionClient;
        this.notificacionClient = notificacionClient;
    }

    public ReservaRespuestaDTO guardar(ReservaDTO dto) {

        log.info(
                "Iniciando registro de reserva. Cliente ID: {}, Habitación ID: {}",
                dto.getIdCliente(),
                dto.getIdHabitacion());

        LocalDate hoy = LocalDate.now();

        if (dto.getFechaInicio().isBefore(hoy)) {

            log.warn(
                    "Fecha de inicio inválida para reserva. Fecha recibida: {}",
                    dto.getFechaInicio());

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de inicio no puede ser anterior a la fecha actual (" + hoy + ")");
        }

        if (dto.getFechaFin().isBefore(dto.getFechaInicio())) {

            log.warn(
                    "Fecha de salida anterior a fecha de entrada. Inicio: {}, Fin: {}",
                    dto.getFechaInicio(),
                    dto.getFechaFin());

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de salida no puede ser anterior a la de entrada");
        }

        ClienteDTO cliente;

        try {

            cliente = clienteClient.obtenerCliente(
                    dto.getIdCliente());

            log.info(
                    "Cliente encontrado ID: {}",
                    dto.getIdCliente());

        } catch (Exception e) {

            log.warn(
                    "Cliente no encontrado al registrar reserva ID: {}",
                    dto.getIdCliente());

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El cliente con ID: " + dto.getIdCliente() + " no existe.");
        }

        HabitacionDTO habitacion;

        try {

            habitacion = habitacionClient.obtenerHabitacion(
                    dto.getIdHabitacion());

            log.info(
                    "Habitación encontrada ID: {}",
                    dto.getIdHabitacion());

        } catch (Exception e) {

            log.warn(
                    "Habitación no encontrada al registrar reserva ID: {}",
                    dto.getIdHabitacion());

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "La Habitación con ID: " + dto.getIdHabitacion() + " no existe.");
        }

        if (!habitacion.isDisponible()) {

            log.warn(
                    "Habitación no disponible por mantenimiento. Habitación ID: {}",
                    dto.getIdHabitacion());

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La habitación con ID: "
                    + dto.getIdHabitacion()
                    + " no está disponible por Mantenimiento.");
        }

        boolean traslape =
                reservaRepository.existeTraslape(
                        dto.getIdHabitacion(),
                        dto.getFechaInicio(),
                        dto.getFechaFin());

        if (traslape) {

            log.warn(
                    "Traslape de reserva detectado. Habitación ID: {}, Inicio: {}, Fin: {}",
                    dto.getIdHabitacion(),
                    dto.getFechaInicio(),
                    dto.getFechaFin());

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La habitación ya está reservada para las fechas seleccionadas.");
        }

        if (!dto.getFechaFin().isAfter(dto.getFechaInicio())) {

            log.warn(
                    "Fecha de salida no posterior a entrada. Inicio: {}, Fin: {}",
                    dto.getFechaInicio(),
                    dto.getFechaFin());

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de salida debe ser posterior a la fecha de entrada");
        }

        long noches =
                ChronoUnit.DAYS.between(
                        dto.getFechaInicio(),
                        dto.getFechaFin());

        double precioPorNoche =
                habitacion.getPrecioPorNoche();

        double total =
                precioPorNoche * noches;

        ReservaModel reserva =
                new ReservaModel();

        reserva.setIdCliente(dto.getIdCliente());
        reserva.setIdHabitacion(dto.getIdHabitacion());
        reserva.setFechaInicio(dto.getFechaInicio());
        reserva.setFechaFin(dto.getFechaFin());
        reserva.setPrecioUnitario(precioPorNoche);
        reserva.setMontoTotal(total);

        ReservaModel reservaGuardada =
                reservaRepository.save(reserva);
        
        NotificacionDTO notificacion = new NotificacionDTO();
                notificacion.setIdCliente(dto.getIdCliente());
                notificacion.setDestinatario("Cliente ID: " + dto.getIdCliente());
                notificacion.setCanal("SISTEMA");
                notificacion.setMensaje(
                        "Su reserva fue registrada correctamente. Reserva ID: "+ reservaGuardada.getId()+ ", habitación ID: "+ dto.getIdHabitacion()+ ", total: $"
                        + reservaGuardada.getMontoTotal()
                );

                notificacionClient.registrarNotificacion(notificacion);

        log.info(
                "Reserva registrada correctamente ID: {}, total: {}",
                reservaGuardada.getId(),
                reservaGuardada.getMontoTotal());

        ReservaRespuestaDTO respuesta =
                new ReservaRespuestaDTO();

        respuesta.setMensaje(
                "La Reserva ha sido Registrada con Éxito!!!");

        return respuesta;
    }

    public List<ReservaModel> listarTodos() {

        log.info("Listando todas las reservas");

        return reservaRepository.findAll();
    }

    public ReservaModel buscarPorId(Long id) {

        log.info(
                "Buscando reserva ID: {}",
                id);

        return reservaRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Reserva no encontrada ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Reserva no encontrada con ID: "
                            + id);
                });
    }

    public ReservaModel modificar(
            Long id,
            ReservaDTO dto) {

        log.info(
                "Modificando reserva ID: {}",
                id);

        ReservaModel reserva =
                reservaRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Reserva no encontrada para modificar ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Reserva no encontrada con ID: "
                            + id);
                });

        LocalDate hoy =
                LocalDate.now();

        if (dto.getFechaInicio().isBefore(hoy)) {

            log.warn(
                    "Fecha de inicio inválida al modificar reserva. Fecha recibida: {}",
                    dto.getFechaInicio());

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de inicio no puede ser anterior a la fecha actual (" + hoy + ")");
        }

        if (!dto.getFechaFin().isAfter(dto.getFechaInicio())) {

            log.warn(
                    "Rango de fechas inválido al modificar reserva. Inicio: {}, Fin: {}",
                    dto.getFechaInicio(),
                    dto.getFechaFin());

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de salida debe ser posterior a la fecha de entrada");
        }

        ClienteDTO cliente =
                clienteClient.obtenerCliente(
                        dto.getIdCliente());

        if (cliente == null) {

            log.warn(
                    "Cliente no encontrado al modificar reserva ID cliente: {}",
                    dto.getIdCliente());

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cliente no encontrado con ID: " + dto.getIdCliente());
        }

        HabitacionDTO habitacion =
                habitacionClient.obtenerHabitacion(
                        dto.getIdHabitacion());

        if (habitacion == null) {

            log.warn(
                    "Habitación no encontrada al modificar reserva ID habitación: {}",
                    dto.getIdHabitacion());

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Habitación no encontrada con ID: " + dto.getIdHabitacion());
        }

        long noches =
                ChronoUnit.DAYS.between(
                        dto.getFechaInicio(),
                        dto.getFechaFin());

        double precioUnitario =
                habitacion.getPrecioPorNoche();

        double montoTotal =
                noches * precioUnitario;

        reserva.setIdCliente(dto.getIdCliente());
        reserva.setIdHabitacion(dto.getIdHabitacion());
        reserva.setFechaInicio(dto.getFechaInicio());
        reserva.setFechaFin(dto.getFechaFin());
        reserva.setPrecioUnitario(precioUnitario);
        reserva.setMontoTotal(montoTotal);

        ReservaModel actualizada =
                reservaRepository.save(reserva);

        log.info(
                "Reserva modificada correctamente ID: {}, nuevo total: {}",
                id,
                actualizada.getMontoTotal());

        return actualizada;
    }

    public void eliminar(Long id) {

        log.warn(
                "Eliminando reserva ID: {}",
                id);

        ReservaModel reserva =
                reservaRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Reserva no encontrada para eliminar ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Reserva no encontrada con ID: "
                            + id);
                });

        reservaRepository.delete(reserva);

        log.info(
                "Reserva eliminada correctamente ID: {}",
                id);
    }
}