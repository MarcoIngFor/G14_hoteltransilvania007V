package com.hoteltransilvania.checkincheckout.controller;

import com.hoteltransilvania.checkincheckout.entity.CheckinCheckout;
import com.hoteltransilvania.checkincheckout.service.CheckinCheckoutService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkin-checkout")
public class CheckinCheckoutController {

    private final CheckinCheckoutService service;

    public CheckinCheckoutController(CheckinCheckoutService service) {
        this.service = service;
    }

    @GetMapping
    public List<CheckinCheckout> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public CheckinCheckout buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<CheckinCheckout> listarPorCliente(@PathVariable Long clienteId) {
        return service.listarPorCliente(clienteId);
    }

    @PostMapping("/checkin")
    public CheckinCheckout realizarCheckin(@Valid @RequestBody CheckinCheckout checkinCheckout) {
        return service.realizarCheckin(checkinCheckout);
    }

    @PutMapping("/checkout/{id}")
    public CheckinCheckout realizarCheckout(@PathVariable Long id) {
        return service.realizarCheckout(id);
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Registro eliminado correctamente";
    }
}