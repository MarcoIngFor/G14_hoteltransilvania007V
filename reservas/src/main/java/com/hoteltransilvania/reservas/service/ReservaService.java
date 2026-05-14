package com.hoteltransilvania.reservas.service;

import com.hoteltransilvania.reservas.dto.ClienteDTO;
import com.hoteltransilvania.reservas.dto.HabitacionDTO;
import com.hoteltransilvania.reservas.dto.ServicioDTO;
import com.hoteltransilvania.reservas.entity.Reserva;
import com.hoteltransilvania.reservas.repository.ReservaRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import lombok.*;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final RestClient restClient;

    @Value("${clientes-service.url}")
    private String clientesServiceUrl;

    @Value("${habitaciones-service.url}")
    private String habitacionesServiceUrl;

    @Value("${servicioextra-service.url}")
    private String servicioextraServiceUrl;

    public ReservaService(
            ReservaRepository reservaRepository,
            RestClient restClient
    ) {
        this.reservaRepository = reservaRepository;
        this.restClient = restClient;
    }

    // ======================================
    // OBTENER TOKEN DEL REQUEST ACTUAL
    // ======================================
    // Este método obtiene el Bearer Token
    // que llega desde Postman hacia reservas-service
    // para reenviarlo a habitaciones-service
    private String obtenerAuthorizationHeader() {

        ServletRequestAttributes attributes =
                (ServletRequestAttributes)
                        RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return null;
        }

        return attributes
                .getRequest()
                .getHeader("Authorization");
    }

    // =========================
    // LISTAR RESERVAS
    // =========================
    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    // =========================
    // CREAR RESERVA
    // =========================
    public Reserva guardar(
            Reserva reserva
    ) {

        // ======================================
        // VALIDAR CLIENTE
        // ======================================
        if (
                reserva.getClienteId() == null ||
                reserva.getClienteId() <= 0
        ) {

            throw new RuntimeException(
                    "ID de cliente inválido: "
                            + reserva.getClienteId()
            );
        }

        // ======================================
        // VALIDAR HABITACIÓN
        // ======================================
        if (
                reserva.getHabitacionId() == null ||
                reserva.getHabitacionId() <= 0
        ) {

            throw new RuntimeException(
                    "ID de habitación inválido: "
                            + reserva.getHabitacionId()
            );
        }

        // ======================================
        // VALIDAR SERVICIO EXTRA
        // ======================================
        if (
                reserva.getServicioId() != null &&
                reserva.getServicioId() <= 0
        ) {

            throw new RuntimeException(
                    "ID de servicio inválido: "
                            + reserva.getServicioId()
            );
        }

        // ======================================
        // BUSCAR CLIENTE
        // ======================================
        ClienteDTO cliente =
                obtenerCliente(
                        reserva.getClienteId()
                );

        // ======================================
        // BUSCAR HABITACIÓN
        // ======================================
        HabitacionDTO habitacion =
                obtenerHabitacion(
                        reserva.getHabitacionId()
                );

        // ======================================
        // BUSCAR SERVICIO EXTRA
        // ======================================
        if (reserva.getServicioId() != null) {

            obtenerServicio(
                    reserva.getServicioId()
            );
        }

        // ======================================
        // VALIDAR DISPONIBILIDAD DE HABITACIÓN
        // ======================================
        if (
                habitacion.getDisponible() == null ||
                !habitacion.getDisponible()
        ) {

            throw new RuntimeException(
                    "La habitación no está disponible"
            );
        }

        // ======================================
        // ESTADO POR DEFECTO
        // ======================================
        if (
                reserva.getEstado() == null ||
                reserva.getEstado().isBlank()
        ) {

            reserva.setEstado("ACTIVA");
        }

        // ======================================
        // GUARDAR RESERVA
        // ======================================
        Reserva reservaGuardada =
                reservaRepository.save(reserva);

        // ======================================
        // MARCAR HABITACIÓN COMO OCUPADA
        // ======================================
        habitacion.setDisponible(false);

        // ======================================
        // ACTUALIZAR HABITACIÓN
        // ======================================
        actualizarHabitacion(
                habitacion.getId(),
                habitacion
        );

        return reservaGuardada;
    }

    // =========================
    // OBTENER RESERVA POR ID
    // =========================
    public Reserva obtenerPorId(
            Long id
    ) {

        return reservaRepository.findById(id)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Reserva no encontrada con ID: "
                                        + id
                        )
                );
    }

    // =========================
    // ELIMINAR RESERVA
    // =========================
    public void eliminar(
            Long id
    ) {

        Reserva reserva =
                obtenerPorId(id);

        HabitacionDTO habitacion =
                obtenerHabitacion(
                        reserva.getHabitacionId()
                );

        // ======================================
        // LIBERAR HABITACIÓN
        // ======================================
        habitacion.setDisponible(true);

        actualizarHabitacion(
                habitacion.getId(),
                habitacion
        );

        reservaRepository.delete(reserva);
    }

    // =========================
    // ACTUALIZAR RESERVA
    // =========================
    public Reserva actualizar(
            Long id,
            Reserva reserva
    ) {

        Reserva existente =
                obtenerPorId(id);

        existente.setClienteId(
                reserva.getClienteId()
        );

        existente.setHabitacionId(
                reserva.getHabitacionId()
        );

        existente.setServicioId(
                reserva.getServicioId()
        );

        existente.setFechaEntrada(
                reserva.getFechaEntrada()
        );

        existente.setFechaSalida(
                reserva.getFechaSalida()
        );

        existente.setEstado(
                reserva.getEstado()
        );

        return reservaRepository.save(existente);
    }

    // =========================
    // CONSULTAR CLIENTE
    // =========================
    private ClienteDTO obtenerCliente(
            Long clienteId
    ) {

        try {

            return restClient.get()

                    .uri(
                            clientesServiceUrl
                                    + "/clientes/{id}",
                            clienteId
                    )

                    .retrieve()

                    .body(ClienteDTO.class);

        } catch (Exception e) {

            throw new RuntimeException(
                    "El cliente no existe con ID: "
                            + clienteId
            );
        }
    }

    // =========================
    // CONSULTAR HABITACIÓN
    // =========================
    private HabitacionDTO obtenerHabitacion(
            Long habitacionId
    ) {

        try {

            // Obtener token actual
            String token =
                    obtenerAuthorizationHeader();

            // Llamar a habitaciones-service
            return restClient.get()

                    .uri(
                            habitacionesServiceUrl
                                    + "/habitaciones/{id}",
                            habitacionId
                    )

                    // Reenviar token JWT
                    .header(
                            "Authorization",
                            token
                    )

                    .retrieve()

                    .body(HabitacionDTO.class);

        } catch (Exception e) {

            throw new RuntimeException(
                    "La habitación no existe con ID: "
                            + habitacionId
            );
        }
    }

    // =========================
    // ACTUALIZAR HABITACIÓN
    // =========================
    private void actualizarHabitacion(
        Long habitacionId,
        HabitacionDTO habitacion
) {

    try {

        // Obtener token recibido en reservas-service
        String token =
                obtenerAuthorizationHeader();

        // Crear body limpio para habitaciones-service
        HabitacionDTO habitacionActualizada =
            new HabitacionDTO(
                    habitacion.getId(),
                    habitacion.getNumero(),
                    habitacion.getTipo(),
                    habitacion.getPrecioPorNoche(),
                    habitacion.getDisponible()
            );

        // Actualizar habitación en habitaciones-service
        restClient.put()
                .uri(
                        habitacionesServiceUrl
                                + "/habitaciones/{id}",
                        habitacionId
                )
                .header(
                        "Authorization",
                        token
                )
                .body(habitacionActualizada)
                .retrieve()
                .body(HabitacionDTO.class);

    } catch (Exception e) {

        throw new RuntimeException(
                "No se pudo actualizar la habitación con ID: "
                        + habitacionId
        );
    }
}

    // =========================
    // CONSULTAR SERVICIO EXTRA
    // =========================
    private ServicioDTO obtenerServicio(
            Long servicioId
    ) {

        try {

            return restClient.get()

                    .uri(
                            servicioextraServiceUrl
                                    + "/servicios/{id}",
                            servicioId
                    )

                    .retrieve()

                    .body(ServicioDTO.class);

        } catch (Exception e) {

            throw new RuntimeException(
                    "El servicio no existe con ID: "
                            + servicioId
            );
        }
    }
}