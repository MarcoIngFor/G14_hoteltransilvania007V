package com.hoteltransilvania.habitaciones.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hoteltransilvania.habitaciones.Model.HabitacionModel;
import com.hoteltransilvania.habitaciones.dto.EstadoHabitacionDTO;
import com.hoteltransilvania.habitaciones.dto.HabitacionDTO;
import com.hoteltransilvania.habitaciones.dto.RespuestaDTO.ActualizarRespuestaDTO;
import com.hoteltransilvania.habitaciones.dto.RespuestaDTO.HabitacionRespuestaDTO;
import com.hoteltransilvania.habitaciones.service.HabitacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Habitaciones", description = "Módulo de gestión de habitaciones del hotel")
@RestController
@RequestMapping("/habitaciones")
public class HabitacionController {

    private final HabitacionService habitacionService;

    public HabitacionController(HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    @Operation(summary = "Crear habitación", description = "Registra una nueva habitación en el sistema. Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Habitación creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para crear habitaciones"),
            @ApiResponse(responseCode = "409", description = "Número de habitación ya registrado")
    })
    @PostMapping
    public ResponseEntity<HabitacionRespuestaDTO> crear(@Valid @RequestBody HabitacionDTO dto) {
        return new ResponseEntity<>(habitacionService.guardar(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar disponibilidad", description = "Modifica la disponibilidad de una habitación. Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para modificar habitaciones"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @PutMapping("/estado/{id}")
    public ResponseEntity<ActualizarRespuestaDTO> actualizarEstado(
            @Parameter(description = "ID de la habitación", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody EstadoHabitacionDTO disponible) {

        habitacionService.actualizarDisponibilidad(id, disponible.isDisponible());

        return ResponseEntity.ok(
                new ActualizarRespuestaDTO("Habitación con id: " + id + " ha sido actualizada"));
    }

    @Operation(summary = "Actualizar habitación", description = "Actualiza los datos de una habitación existente. Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitación actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar habitaciones"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada"),
            @ApiResponse(responseCode = "409", description = "Número de habitación ya registrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<HabitacionModel> actualizar(
            @Parameter(description = "ID de la habitación que se desea actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody HabitacionDTO dto) {

        HabitacionModel habitacionActualizada = habitacionService.actualizar(id, dto);
        return new ResponseEntity<>(habitacionActualizada, HttpStatus.OK);
    }

    @Operation(summary = "Listar habitaciones", description = "Retorna todas las habitaciones registradas. Requiere rol ADMIN o RECEPCIONISTA.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones listadas correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping
    public ResponseEntity<List<HabitacionModel>> listar() {
        List<HabitacionModel> habitaciones = habitacionService.listarTodos();
        return new ResponseEntity<>(habitaciones, HttpStatus.OK);
    }

    @Operation(summary = "Obtener habitación por ID", description = "Retorna una habitación específica según su ID. Requiere rol ADMIN o RECEPCIONISTA.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitación encontrada"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HabitacionModel> buscarPorId(
            @Parameter(description = "ID de la habitación", example = "1")
            @PathVariable Long id) {

        HabitacionModel habitacion = habitacionService.buscarPorId(id);
        return new ResponseEntity<>(habitacion, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar habitación", description = "Elimina una habitación del sistema. Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitación eliminada correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar habitaciones"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HabitacionRespuestaDTO> eliminar(
            @Parameter(description = "ID de la habitación que se desea eliminar", example = "1")
            @PathVariable Long id) {

        habitacionService.eliminar(id);

        return ResponseEntity.ok(
                new HabitacionRespuestaDTO("La habitación de id: " + id + ", ha sido eliminada"));
    }
}