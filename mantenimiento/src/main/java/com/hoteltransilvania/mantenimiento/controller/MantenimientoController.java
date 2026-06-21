package com.hoteltransilvania.mantenimiento.controller;

import com.hoteltransilvania.mantenimiento.dto.MantenimientoDTO;
import com.hoteltransilvania.mantenimiento.dto.RespuestaDTO.MantenimientoRespuestaDTO;
import com.hoteltransilvania.mantenimiento.model.MantenimientoModel;
import com.hoteltransilvania.mantenimiento.service.MantenimientoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Mantenimiento", description = "Módulo de gestión de mantenimientos de habitaciones")
@RestController
@RequestMapping("/mantenimiento")
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    public MantenimientoController(MantenimientoService mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
    }

    @Operation(
            summary = "Crear mantenimiento",
            description = "Registra un nuevo mantenimiento asociado a una habitación. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Mantenimiento registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para crear mantenimientos"),
            @ApiResponse(responseCode = "404", description = "Habitación asociada no encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos")
    })
    @PostMapping
    public ResponseEntity<MantenimientoRespuestaDTO> crear(@Valid @RequestBody MantenimientoDTO dto) {

        MantenimientoModel mantenimiento = mantenimientoService.guardar(dto);

        MantenimientoRespuestaDTO respuesta = new MantenimientoRespuestaDTO();
        respuesta.setMensaje("Mantenimiento de la habitación: "
                + mantenimiento.getIdHabitacion()
                + ", registrado correctamente");
        respuesta.setDescripcion(mantenimiento.getDescripcion());
        respuesta.setEstado(mantenimiento.getEstado());

        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar mantenimientos",
            description = "Retorna todos los mantenimientos registrados. Requiere rol ADMIN o RECEPCIONISTA."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mantenimientos listados correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping
    public ResponseEntity<List<MantenimientoModel>> listar() {
        return new ResponseEntity<>(mantenimientoService.listarTodos(), HttpStatus.OK);
    }

    @Operation(
            summary = "Obtener mantenimiento por ID",
            description = "Retorna un mantenimiento específico según su ID. Requiere rol ADMIN o RECEPCIONISTA."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mantenimiento encontrado"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder"),
            @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MantenimientoModel> buscarPorId(
            @Parameter(description = "ID del mantenimiento", example = "1")
            @PathVariable Long id) {

        return new ResponseEntity<>(mantenimientoService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Eliminar mantenimiento",
            description = "Elimina un mantenimiento del sistema según su ID. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Mantenimiento eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar mantenimientos"),
            @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del mantenimiento que se desea eliminar", example = "1")
            @PathVariable Long id) {

        mantenimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Modificar estado de mantenimiento",
            description = "Actualiza únicamente el estado de un mantenimiento existente. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado del mantenimiento actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Estado inválido"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para modificar el estado"),
            @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado")
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<MantenimientoRespuestaDTO> modificarEstado(
            @Parameter(description = "ID del mantenimiento", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado del mantenimiento", example = "FINALIZADO")
            @RequestParam String estado) {

        MantenimientoModel mantenimiento = mantenimientoService.modificarEstado(id, estado);

        MantenimientoRespuestaDTO respuesta = new MantenimientoRespuestaDTO();
        respuesta.setMensaje("Estado del mantenimiento actualizado correctamente");
        respuesta.setDescripcion(mantenimiento.getDescripcion());
        respuesta.setEstado(mantenimiento.getEstado());

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Operation(
            summary = "Actualizar mantenimiento",
            description = "Actualiza todos los datos de un mantenimiento existente. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mantenimiento actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar mantenimientos"),
            @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MantenimientoModel> actualizar(
            @Parameter(description = "ID del mantenimiento que se desea actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody MantenimientoDTO dto) {

        MantenimientoModel actualizado = mantenimientoService.actualizar(id, dto);

        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }
}