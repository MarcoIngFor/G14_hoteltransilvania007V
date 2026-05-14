package com.hoteltransilvania.habitacionesservice.controller;

import com.hoteltransilvania.habitacionesservice.entity.Habitacion;
import com.hoteltransilvania.habitacionesservice.service.HabitacionService;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habitaciones")
public class HabitacionController {

    private final HabitacionService habitacionService;

    public HabitacionController(
            HabitacionService habitacionService
    ) {
        this.habitacionService = habitacionService;
    }

    // =========================
    // VER TODAS LAS HABITACIONES
    // =========================
    @PreAuthorize("hasAuthority('VER_HABITACIONES')")
    @GetMapping
    public List<Habitacion> listar() {

        return habitacionService.listar();
    }
    // =========================
    // CREAR HABITACIÓN
    // =========================
    @PreAuthorize("hasAuthority('CREAR_HABITACION')")
    @PostMapping
    public Habitacion guardar(
            @Valid @RequestBody Habitacion habitacion
    ) {

        return habitacionService.guardar(habitacion);
    }

    
    // =========================
    // VER HABITACIÓN POR ID
    @PreAuthorize("hasAuthority('VER_HABITACIONES')")
    @GetMapping("/{id}")
    public Habitacion obtenerPorId(
            @PathVariable Long id
    ) {

        return habitacionService.obtenerPorId(id);
    }

    // =========================
    // ELIMINAR HABITACIÓN
    // =========================
    @PreAuthorize("hasAuthority('ELIMINAR_HABITACION')")
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id
    ) {

        habitacionService.eliminar(id);
    }

    // =========================
    // ACTUALIZAR HABITACIÓN
    // =========================
    @PreAuthorize("hasAuthority('EDITAR_HABITACIONES')")
    @PutMapping("/{id}")
    public Habitacion actualizar(
            @PathVariable Long id,
            @RequestBody Habitacion habitacion
    ) {

        return habitacionService.actualizar(id, habitacion);
    }
}