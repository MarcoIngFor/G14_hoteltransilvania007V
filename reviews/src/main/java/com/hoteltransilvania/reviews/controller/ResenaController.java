package com.hoteltransilvania.reviews.controller;

import com.hoteltransilvania.reviews.entity.Resena;
import com.hoteltransilvania.reviews.service.ResenaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resenas")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(
            ResenaService resenaService
    ) {
        this.resenaService = resenaService;
    }

    // VER TODAS LAS RESEÑAS
    @GetMapping
    public List<Resena> listar() {
        return resenaService.listar();
    }

    // VER RESEÑA POR ID
    @GetMapping("/{id}")
    public Resena obtenerPorId(
            @PathVariable Long id
    ) {
        return resenaService.obtenerPorId(id);
    }

    // VER RESEÑAS POR HABITACIÓN
    @GetMapping("/habitacion/{habitacionId}")
    public List<Resena> buscarPorHabitacion(
            @PathVariable Long habitacionId
    ) {
        return resenaService.buscarPorHabitacion(habitacionId);
    }

    // CREAR RESEÑA
    @PostMapping
    public Resena guardar(
            @Valid @RequestBody Resena resena
    ) {
        return resenaService.guardar(resena);
    }

    // ELIMINAR RESEÑA
    @DeleteMapping("/{id}")
    public String eliminar(
            @PathVariable Long id
    ) {
        resenaService.eliminar(id);

        return "Reseña eliminada correctamente";
    }
}