package com.hoteltransilvania.servicioextra.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hoteltransilvania.servicioextra.Model.ServicioExtraModel;
import com.hoteltransilvania.servicioextra.dto.ServicioExtraDTO;
import com.hoteltransilvania.servicioextra.dto.RespuestaDTO.SerExtraDTO;
import com.hoteltransilvania.servicioextra.service.ServicioExtraService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Servicios Extra", description = "Módulo de gestión de servicios extra del hotel")
@RestController
@RequestMapping("/servicioextra")
public class ServicioExtraController {

    private final ServicioExtraService servicioExtraService;

    public ServicioExtraController(ServicioExtraService servicioExtraService) {
        this.servicioExtraService = servicioExtraService;
    }

    @Operation(summary = "Listar servicios extra", description = "Retorna todos los servicios extra registrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Servicios extra listados correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<ServicioExtraModel>> listar() {
        return new ResponseEntity<>(servicioExtraService.listarTodos(), HttpStatus.OK);
    }

    @Operation(summary = "Obtener servicio extra por ID", description = "Retorna un servicio extra específico según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Servicio extra encontrado"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder"),
            @ApiResponse(responseCode = "404", description = "Servicio extra no encontrado")
    })
    @GetMapping("/listar/{id}")
    public ResponseEntity<?> listarxId(
            @Parameter(description = "ID del servicio extra", example = "1")
            @PathVariable Long id) {

        return new ResponseEntity<>(servicioExtraService.obtenerPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Registrar servicio extra", description = "Registra un nuevo servicio extra. Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Servicio extra registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos"),
            @ApiResponse(responseCode = "409", description = "Servicio extra ya registrado")
    })
    @PostMapping("/registrar")
    public ResponseEntity<SerExtraDTO> guardar(@Valid @RequestBody ServicioExtraDTO dto) {
        return new ResponseEntity<>(servicioExtraService.guardar(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar servicio extra", description = "Actualiza un servicio extra existente. Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Servicio extra actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos"),
            @ApiResponse(responseCode = "404", description = "Servicio extra no encontrado"),
            @ApiResponse(responseCode = "409", description = "Servicio extra ya registrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ServicioExtraModel> actualizar(
            @Parameter(description = "ID del servicio extra", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ServicioExtraDTO dto) {

        return new ResponseEntity<>(servicioExtraService.actualizar(id, dto), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar servicio extra", description = "Elimina un servicio extra por ID. Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Servicio extra eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos"),
            @ApiResponse(responseCode = "404", description = "Servicio extra no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SerExtraDTO> eliminar(
            @Parameter(description = "ID del servicio extra", example = "1")
            @PathVariable Long id) {

        servicioExtraService.eliminar(id);
        return ResponseEntity.ok(new SerExtraDTO("Servicio con id: " + id + ", ha sido eliminado"));
    }
}