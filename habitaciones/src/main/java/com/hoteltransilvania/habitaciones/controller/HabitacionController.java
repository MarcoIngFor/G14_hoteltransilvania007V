package com.hoteltransilvania.habitaciones.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoteltransilvania.habitaciones.Model.HabitacionModel;
import com.hoteltransilvania.habitaciones.dto.HabitacionDTO;
import com.hoteltransilvania.habitaciones.dto.RespuestaDTO.HabitacionRespuestaDTO;
import com.hoteltransilvania.habitaciones.service.HabitacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

import java.util.List;




@RestController
@RequestMapping("/habitaciones")
public class HabitacionController {

    @Autowired
    private HabitacionService habitacionService;

    @PostMapping
    public ResponseEntity<HabitacionRespuestaDTO> crear (@Valid @RequestBody HabitacionDTO dto){
        return new ResponseEntity<>(habitacionService.guardar(dto),HttpStatus.CREATED);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Void>actualizarEstado(@PathVariable Long id, @RequestParam boolean disponible){
        habitacionService.actualizarDisponibilidad(id,disponible);
        return ResponseEntity.noContent().build();
    }
    
    
    @GetMapping
    public ResponseEntity<List<HabitacionModel>> listar() {
        List<HabitacionModel> habitaciones = habitacionService.listarTodos();
        return new ResponseEntity<>(habitaciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionModel> buscarPorId(@PathVariable Long id) {
        HabitacionModel habitaciones = habitacionService.buscarPorId(id);
        return new ResponseEntity<>(habitaciones, HttpStatus.OK);
    }
}
