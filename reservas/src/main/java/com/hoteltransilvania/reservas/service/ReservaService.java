package com.hoteltransilvania.reservas.service;

import com.hoteltransilvania.reservas.dto.ClienteDTO;
import com.hoteltransilvania.reservas.dto.HabitacionDTO;
import org.springframework.web.client.RestClient;
import com.hoteltransilvania.reservas.entity.Reserva;
import com.hoteltransilvania.reservas.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final RestClient restClient;

    public ReservaService(ReservaRepository reservaRepository, RestClient restClient) {
        this.reservaRepository = reservaRepository;
        this.restClient = restClient;
    }

    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    public Reserva guardar(Reserva reserva) {

        if(reserva.getClienteId()<=0){
            throw new RuntimeException("ID de cliente Inválido: "+reserva.getClienteId());
        }

        ClienteDTO cliente = restClient.get()
                .uri("http://localhost:8080/clientes/{id}", reserva.getClienteId())
                .retrieve()
                .body(ClienteDTO.class);

        if (cliente == null) {
            throw new RuntimeException("El cliente no existe");
        }



        HabitacionDTO habitacion = restClient.get()
                .uri("http://localhost:8082/habitaciones/{id}", reserva.getHabitacionId())
                .retrieve()
                .body(HabitacionDTO.class);

        if (habitacion == null) {
            throw new RuntimeException("La habitación no existe");
        }

        if (!habitacion.isDisponible()) {
            throw new RuntimeException("La habitación no está disponible");
        }

        habitacion.setDisponible(false);

        restClient.put()
                .uri("http://localhost:8082/habitaciones/{id}", habitacion.getId())
                .body(habitacion)
                .retrieve()
                .body(HabitacionDTO.class);
        
        Reserva reservaGuardada = reservaRepository.save(reserva);

        return reservaGuardada;
    }

    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        reservaRepository.deleteById(id);
    }

    public Reserva actualizar(Long id, Reserva reserva) {
        Reserva existente = reservaRepository.findById(id).orElse(null);

        if (existente == null) {
            return null;
        }

        existente.setClienteId(reserva.getClienteId());
        existente.setHabitacionId(reserva.getHabitacionId());
        existente.setFechaEntrada(reserva.getFechaEntrada());
        existente.setFechaSalida(reserva.getFechaSalida());
        existente.setEstado(reserva.getEstado());

        return reservaRepository.save(existente);
    }
}
