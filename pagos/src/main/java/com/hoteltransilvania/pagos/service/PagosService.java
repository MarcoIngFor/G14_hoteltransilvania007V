package com.hoteltransilvania.pagos.service;

import com.hoteltransilvania.pagos.client.ConsumoClient;
import com.hoteltransilvania.pagos.client.ReservaClient;
import com.hoteltransilvania.pagos.dto.ConsumoDTO;
import com.hoteltransilvania.pagos.dto.PagosDTO;
import com.hoteltransilvania.pagos.dto.ReservaDTO;
import com.hoteltransilvania.pagos.exception.ResourceNotFoundException;
import com.hoteltransilvania.pagos.model.PagosModel;
import com.hoteltransilvania.pagos.repository.PagosRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PagosService {

    private final PagosRepository pagosRepository;
    private final ReservaClient reservaClient;
    private final ConsumoClient consumoClient;

    public PagosService(
            PagosRepository pagosRepository,
            ReservaClient reservaClient,
            ConsumoClient consumoClient) {

        this.pagosRepository = pagosRepository;
        this.reservaClient = reservaClient;
        this.consumoClient = consumoClient;
    }

    public List<PagosModel> listar() {

        log.info("Listando todos los pagos");

        return pagosRepository.findAll();
    }

    public PagosModel buscarPorId(Long id) {

        log.info(
                "Buscando pago ID: {}",
                id);

        return pagosRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Pago no encontrado ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Pago no encontrado con ID: "
                            + id);
                });
    }

    public PagosModel buscarPorReserva(Long idReserva) {

        log.info(
                "Buscando pago por reserva ID: {}",
                idReserva);

        return pagosRepository.findByIdReserva(idReserva)
                .orElseThrow(() -> {

                    log.warn(
                            "No existe pago asociado a reserva ID: {}",
                            idReserva);

                    return new ResourceNotFoundException(
                            "No existe pago para la reserva ID: "
                            + idReserva);
                });
    }

    public PagosModel registrar(PagosDTO dto) {

        log.info(
                "Iniciando registro de pago para reserva ID: {}",
                dto.getIdReserva());

        if (pagosRepository.findByIdReserva(
                dto.getIdReserva()).isPresent()) {

            log.warn(
                    "Intento de pago duplicado para reserva ID: {}",
                    dto.getIdReserva());

            throw new ResourceNotFoundException(
                    "Ya existe un pago registrado para la reserva ID: "
                    + dto.getIdReserva());
        }

        ReservaDTO reserva =
                reservaClient.obtenerReserva(
                        dto.getIdReserva());

        log.info(
                "Reserva encontrada ID: {}",
                reserva.getId());

        double subtotalHabitacion =
                reserva.getMontoTotal();

        List<ConsumoDTO> consumos =
                consumoClient.obtenerConsumosReserva(
                        reserva.getId());

        double subtotalServicios =
                consumos.stream()
                .mapToDouble(
                        ConsumoDTO::getTotalConsumo)
                .sum();

        double totalPagar =
                subtotalHabitacion
                + subtotalServicios;

        PagosModel pago =
                new PagosModel();

        pago.setIdReserva(reserva.getId());
        pago.setSubtotalHabitacion(
                subtotalHabitacion);
        pago.setSubtotalServicios(
                subtotalServicios);
        pago.setTotalPagar(
                totalPagar);
        pago.setMetodoPago(
                dto.getMetodoPago());
        pago.setEstadoPago(
                "PAGADO");
        pago.setFechaPago(
                LocalDate.now());

        PagosModel pagoGuardado =
                pagosRepository.save(pago);

        log.info(
                "Pago registrado correctamente ID: {}, total: {}",
                pagoGuardado.getId(),
                pagoGuardado.getTotalPagar());

        return pagoGuardado;
    }

    public PagosModel actualizar(
            Long id,
            PagosDTO dto) {

        log.info(
                "Actualizando pago ID: {}",
                id);

        PagosModel pago =
                pagosRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Pago no encontrado para actualización ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Pago no encontrado con ID: "
                            + id);
                });

        pago.setMetodoPago(
                dto.getMetodoPago());

        pago.setIdReserva(
                dto.getIdReserva());

        PagosModel actualizado =
                pagosRepository.save(pago);

        log.info(
                "Pago actualizado correctamente ID: {}",
                id);

        return actualizado;
    }

    public void eliminar(Long id) {

        log.warn(
                "Eliminando pago ID: {}",
                id);

        PagosModel pago =
                pagosRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Pago no encontrado para eliminar ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Pago no encontrado con ID: "
                            + id);
                });

        pagosRepository.delete(pago);

        log.info(
                "Pago eliminado correctamente ID: {}",
                id);
    }
}