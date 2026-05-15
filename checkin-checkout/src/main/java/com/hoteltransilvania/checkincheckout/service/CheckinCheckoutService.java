package com.hoteltransilvania.checkincheckout.service;

import com.hoteltransilvania.checkincheckout.entity.CheckinCheckout;
import com.hoteltransilvania.checkincheckout.entity.CheckinCheckout.EstadoCheckinCheckout;
import com.hoteltransilvania.checkincheckout.exception.ResourceNotFoundException;
import com.hoteltransilvania.checkincheckout.repository.CheckinCheckoutRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckinCheckoutService {

    private final CheckinCheckoutRepository repository;

    public CheckinCheckoutService(CheckinCheckoutRepository repository) {
        this.repository = repository;
    }

    public List<CheckinCheckout> listar() {
        return repository.findAll();
    }

    public CheckinCheckout buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro no encontrado"));
    }

    public List<CheckinCheckout> listarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    public CheckinCheckout realizarCheckin(CheckinCheckout checkinCheckout) {

        checkinCheckout.setFechaCheckin(LocalDateTime.now());

        checkinCheckout.setEstado(
                EstadoCheckinCheckout.CHECKIN_REALIZADO
        );

        return repository.save(checkinCheckout);
    }

    public CheckinCheckout realizarCheckout(Long id) {

        CheckinCheckout registro = buscarPorId(id);

        registro.setFechaCheckout(LocalDateTime.now());

        registro.setEstado(
                EstadoCheckinCheckout.CHECKOUT_REALIZADO
        );

        return repository.save(registro);
    }

    public void eliminar(Long id) {

        CheckinCheckout registro = buscarPorId(id);

        repository.delete(registro);
    }
}