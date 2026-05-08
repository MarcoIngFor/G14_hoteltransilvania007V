package com.hoteltransilvania.servicioextra.controller;

import com.hoteltransilvania.servicioextra.entity.Servicio;
import com.hoteltransilvania.servicioextra.service.ServicioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    public List<Servicio> listar() {
        return servicioService.listar();
    }

    @GetMapping("/{id}")
    public Servicio obtenerPorId(@PathVariable Long id) {
        return servicioService.obtenerPorId(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Servicio guardar(@Valid @RequestBody Servicio servicio) {
        return servicioService.guardar(servicio);
    }

    @PutMapping("/{id}")
    public Servicio actualizar(@PathVariable Long id, @Valid @RequestBody Servicio servicio) {
        return servicioService.actualizar(id, servicio);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicioService.eliminar(id);
    }
}