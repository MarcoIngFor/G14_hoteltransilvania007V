package com.hoteltransilvania.reservas.service;

import com.hoteltransilvania.reservas.entity.Reserva;
import com.hoteltransilvania.reservas.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    public Reserva guardar(Reserva reserva) {
        return reservaRepository.save(reserva);
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
