package com.hoteltransilvania.reservas.controller;

import com.hoteltransilvania.reservas.entity.Reserva;
import com.hoteltransilvania.reservas.service.ReservaService;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(
            ReservaService reservaService
    ) {
        this.reservaService = reservaService;
    }

    // =========================
    // VER TODAS LAS RESERVAS
    // =========================
    @PreAuthorize("hasAuthority('VER_RESERVAS')")
    @GetMapping
    public List<Reserva> listar() {

        return reservaService.listar();
    }

    // =========================
    // CREAR RESERVA
    // =========================
    @PreAuthorize("hasAuthority('CREAR_RESERVA')")
    @PostMapping
    public Reserva guardar(
            @Valid @RequestBody Reserva reserva
    ) {

        return reservaService.guardar(reserva);
    }

    // =========================
    // VER RESERVA POR ID
    // =========================
    @PreAuthorize("hasAuthority('VER_RESERVAS')")
    @GetMapping("/{id}")
    public Reserva obtenerPorId(
            @PathVariable Long id
    ) {

        return reservaService.obtenerPorId(id);
    }

    // =========================
    // ELIMINAR / CANCELAR RESERVA
    // =========================
    @PreAuthorize("hasAuthority('ELIMINAR_RESERVA')")
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id
    ) {

        reservaService.eliminar(id);
    }

    // =========================
    // ACTUALIZAR RESERVA
    // =========================
    @PreAuthorize("hasAuthority('EDITAR_RESERVA')")
    @PutMapping("/{id}")
    public Reserva actualizar(
            @PathVariable Long id,
            @RequestBody Reserva reserva
    ) {

        return reservaService.actualizar(id, reserva);
    }
}