package com.hoteltransilvania.servicioextra.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hoteltransilvania.servicioextra.Model.TipoServicioModel;
import com.hoteltransilvania.servicioextra.dto.TipoServicioDTO;
import com.hoteltransilvania.servicioextra.dto.RespuestaDTO.SerExtraDTO;
import com.hoteltransilvania.servicioextra.service.TipoServicioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Tipos de Servicio", description = "Módulo de gestión de tipos de servicios extra")
@RestController
@RequestMapping("/tiposervicio")
public class TipoServicioController {

    private final TipoServicioService tipoServicioService;

    public TipoServicioController(TipoServicioService tipoServicioService) {
        this.tipoServicioService = tipoServicioService;
    }

    @Operation(summary = "Listar tipos de servicio", description = "Retorna todos los tipos de servicio registrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipos de servicio listados correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<TipoServicioModel>> listar() {
        return new ResponseEntity<>(tipoServicioService.listarTodos(), HttpStatus.OK);
    }

    @Operation(summary = "Registrar tipo de servicio", description = "Registra un nuevo tipo de servicio. Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo de servicio registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos"),
            @ApiResponse(responseCode = "409", description = "Tipo de servicio ya registrado")
    })
    @PostMapping("/registrar")
    public ResponseEntity<SerExtraDTO> guardar(@Valid @RequestBody TipoServicioDTO dto) {
        return new ResponseEntity<>(tipoServicioService.guardar(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar tipo de servicio", description = "Elimina un tipo de servicio por ID. Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de servicio eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos"),
            @ApiResponse(responseCode = "404", description = "Tipo de servicio no encontrado")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<SerExtraDTO> eliminar(
            @Parameter(description = "ID del tipo de servicio", example = "1")
            @PathVariable Long id) {

        tipoServicioService.eliminar(id);
        return ResponseEntity.ok(new SerExtraDTO("Registro con id: " + id + " eliminado"));
    }
}