package com.hoteltransilvania.consumo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hoteltransilvania.consumo.client.ClienteClient;
import com.hoteltransilvania.consumo.client.HabitacionClient;
import com.hoteltransilvania.consumo.client.ReservaClient;
import com.hoteltransilvania.consumo.client.ServicioExtraClient;
import com.hoteltransilvania.consumo.dto.ConsumoDTO;
import com.hoteltransilvania.consumo.dto.ServicioExtraDTO;
import com.hoteltransilvania.consumo.dto.RespuestaDTO.ConsumoRespuestaDTO;
import com.hoteltransilvania.consumo.exception.ResourceNotFoundException;
import com.hoteltransilvania.consumo.model.ConsumoModel;
import com.hoteltransilvania.consumo.repository.ConsumoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsumoService {

    private final ConsumoRepository consumoRepository;
    private final ServicioExtraClient servicioExtraClient;
    private final ClienteClient clienteClient;
    private final HabitacionClient habitacionClient;
    private final ReservaClient reservaClient;

    public ConsumoService(
            ConsumoRepository consumoRepository,
            ServicioExtraClient servicioExtraClient,
            ClienteClient clienteClient,
            HabitacionClient habitacionClient,
            ReservaClient reservaClient) {

        this.consumoRepository = consumoRepository;
        this.servicioExtraClient = servicioExtraClient;
        this.clienteClient = clienteClient;
        this.habitacionClient = habitacionClient;
        this.reservaClient = reservaClient;
    }

    public ConsumoRespuestaDTO guardar(ConsumoDTO dto){

        log.info("Iniciando registro de consumo para reserva ID: {}", dto.getIdReserva());

        //Verifica la existencia
        clienteClient.buscarPorId(dto.getIdCliente()); //Si se cumple continua
        habitacionClient.buscarPorId(dto.getIdHabitacion());//Si se cumple continua
        reservaClient.buscarPorId(dto.getIdReserva());//Si se cumple continua

        ServicioExtraDTO servicioExtra = servicioExtraClient.buscarPorId(dto.getIdServicioExtra());

        log.info("Servicio extra encontrado ID: {}", dto.getIdServicioExtra());

        double total = servicioExtra.getPrecio() * dto.getCantidad();

        ConsumoModel consumo = new ConsumoModel();

        consumo.setIdServicioExtra(dto.getIdServicioExtra());
        consumo.setIdReserva(dto.getIdReserva());
        consumo.setIdCliente(dto.getIdCliente());
        consumo.setIdHabitacion(dto.getIdHabitacion());
        consumo.setCantidad(dto.getCantidad());
        consumo.setTotalConsumo(total);
        consumo.setFecha(LocalDate.now());

        ConsumoModel consumoGuardado =
                consumoRepository.save(consumo);

        log.info(
                "Consumo registrado correctamente ID: {}, total: {}",
                consumoGuardado.getId(),
                consumoGuardado.getTotalConsumo());

        ConsumoRespuestaDTO respuesta =
                new ConsumoRespuestaDTO();

        respuesta.setMessage("Consumo registrado correctamente");
        respuesta.setIdServicioExtra(consumo.getIdServicioExtra());
        respuesta.setIdCliente(consumo.getIdCliente());
        respuesta.setIdReserva(consumo.getIdReserva());

        return respuesta;
    }

    public List<ConsumoModel> listarTodos(){

        log.info("Listando todos los consumos");

        return consumoRepository.findAll();
    }

    public ConsumoModel buscarPorId(Long id){

        log.info("Buscando consumo ID: {}", id);

        return consumoRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Consumo no encontrado ID: {}", id);

                    return new ResourceNotFoundException(
                            "Consumo no encontrado con ID: " + id);
                });
    }

    public List<ConsumoModel> buscarPorCliente(Long idCliente){

        log.info("Buscando consumos por cliente ID: {}", idCliente);

        return consumoRepository.findByIdCliente(idCliente);
    }

    public List<ConsumoModel> buscarPorReserva(Long idReserva){

        log.info("Buscando consumos por reserva ID: {}", idReserva);

        return consumoRepository.findByIdReserva(idReserva);
    }

    public List<ConsumoModel> buscarPorHabitacion(Long idHabitacion){

        log.info("Buscando consumos por habitación ID: {}", idHabitacion);

        return consumoRepository.findByIdHabitacion(idHabitacion);
    }

    public void eliminar(Long id){

        log.warn("Eliminando consumo ID: {}", id);

        ConsumoModel consumo = buscarPorId(id);

        consumoRepository.delete(consumo);

        log.info("Consumo eliminado correctamente ID: {}", id);
    }

    public ConsumoModel actualizar(Long id, ConsumoDTO dto) {

        log.info("Actualizando consumo ID: {}", id);

        ConsumoModel consumo = consumoRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Consumo no encontrado para actualizar ID: {}", id);

                    return new ResourceNotFoundException(
                            "Consumo no encontrado con ID: " + id);
                });

        clienteClient.buscarPorId(dto.getIdCliente());
        habitacionClient.buscarPorId(dto.getIdHabitacion());
        reservaClient.buscarPorId(dto.getIdReserva());

        ServicioExtraDTO servicioExtra =
                servicioExtraClient.buscarPorId(dto.getIdServicioExtra());

        double total = servicioExtra.getPrecio() * dto.getCantidad();

        consumo.setCantidad(dto.getCantidad());
        consumo.setIdCliente(dto.getIdCliente());
        consumo.setIdReserva(dto.getIdReserva());
        consumo.setIdHabitacion(dto.getIdHabitacion());
        consumo.setIdServicioExtra(dto.getIdServicioExtra());
        consumo.setTotalConsumo(total);

        ConsumoModel consumoActualizado =
                consumoRepository.save(consumo);

        log.info("Consumo actualizado correctamente ID: {}", id);

        return consumoActualizado;
    }
}