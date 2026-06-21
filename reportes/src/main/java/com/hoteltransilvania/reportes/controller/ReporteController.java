package com.hoteltransilvania.reportes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hoteltransilvania.reportes.dto.ReporteDTO;
import com.hoteltransilvania.reportes.model.ReporteModel;
import com.hoteltransilvania.reportes.service.ReporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Reportes", description = "Módulo de gestión de reportes del sistema hotelero")
@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @Operation(
            summary = "Registrar reporte",
            description = "Registra un nuevo reporte en el sistema. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reporte registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para registrar reportes")
    })
    @PostMapping("/registrar")
    public ResponseEntity<ReporteModel> guardar(@Valid @RequestBody ReporteDTO dto) {
        return new ResponseEntity<>(reporteService.guardar(dto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar reportes",
            description = "Retorna todos los reportes registrados. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reportes listados correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<ReporteModel>> listarTodos() {
        return ResponseEntity.ok(reporteService.listarTodos());
    }

    @Operation(
            summary = "Obtener reporte por ID",
            description = "Retorna un reporte específico según su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte encontrado"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReporteModel> buscarPorId(
            @Parameter(description = "ID del reporte", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(reporteService.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar reportes por tipo",
            description = "Retorna los reportes filtrados por tipo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reportes filtrados correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ReporteModel>> buscarPorTipo(
            @Parameter(description = "Tipo de reporte", example = "RESERVAS")
            @PathVariable String tipo) {

        return ResponseEntity.ok(reporteService.buscarPorTipo(tipo));
    }

    @Operation(
            summary = "Actualizar reporte",
            description = "Actualiza los datos de un reporte existente. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar reportes"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReporteModel> actualizar(
            @Parameter(description = "ID del reporte que se desea actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ReporteDTO dto) {

        return ResponseEntity.ok(reporteService.actualizar(id, dto));
    }

    @Operation(
            summary = "Eliminar reporte",
            description = "Elimina un reporte del sistema según su ID. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reporte eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar reportes"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del reporte que se desea eliminar", example = "1")
            @PathVariable Long id) {

        reporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}