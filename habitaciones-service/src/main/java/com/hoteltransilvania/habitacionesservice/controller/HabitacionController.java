package com.hoteltransilvania.habitacionesservice.controller;

import com.hoteltransilvania.habitacionesservice.entity.Habitacion;
import com.hoteltransilvania.habitacionesservice.service.HabitacionService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habitaciones")
public class HabitacionController {

    private final HabitacionService habitacionService;

    public HabitacionController(HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    @GetMapping
    public List<Habitacion> listar() {
        return habitacionService.listar();
    }

    @PostMapping
    public Habitacion guardar(@Valid @RequestBody Habitacion habitacion) {
        return habitacionService.guardar(habitacion);
    }

    @GetMapping("/{id}")
    public Habitacion obtenerPorId(@PathVariable Long id) {
        return habitacionService.obtenerPorId(id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        habitacionService.eliminar(id);
    }

    @PutMapping("/{id}")
    public Habitacion actualizar(@PathVariable Long id, @RequestBody Habitacion habitacion) {
        return habitacionService.actualizar(id, habitacion);
    }
}