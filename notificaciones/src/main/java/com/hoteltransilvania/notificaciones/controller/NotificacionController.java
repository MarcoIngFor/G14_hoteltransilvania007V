package com.hoteltransilvania.notificaciones.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hoteltransilvania.notificaciones.dto.NotificacionDTO;
import com.hoteltransilvania.notificaciones.model.NotificacionModel;
import com.hoteltransilvania.notificaciones.service.NotificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Notificaciones", description = "Módulo de gestión de notificaciones del sistema")
@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @Operation(
            summary = "Registrar notificación",
            description = "Registra una nueva notificación asociada a un cliente. Requiere rol ADMIN o RECEPCIONISTA."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Notificación registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para registrar notificaciones"),
            @ApiResponse(responseCode = "404", description = "Cliente asociado no encontrado")
    })
    @PostMapping("/registrar")
    public ResponseEntity<NotificacionModel> guardar(@Valid @RequestBody NotificacionDTO dto) {
        return new ResponseEntity<>(notificacionService.guardar(dto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar notificaciones",
            description = "Retorna todas las notificaciones registradas. Requiere rol ADMIN o RECEPCIONISTA."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificaciones listadas correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<NotificacionModel>> listarTodos() {
        return ResponseEntity.ok(notificacionService.listarTodos());
    }

    @Operation(
            summary = "Obtener notificación por ID",
            description = "Retorna una notificación específica según su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionModel> buscarPorId(
            @Parameter(description = "ID de la notificación", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(notificacionService.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar notificaciones por cliente",
            description = "Retorna las notificaciones asociadas a un cliente específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificaciones del cliente listadas correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<NotificacionModel>> buscarPorCliente(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long idCliente) {

        return ResponseEntity.ok(notificacionService.buscarPorCliente(idCliente));
    }

    @Operation(
            summary = "Buscar notificaciones por estado",
            description = "Retorna las notificaciones filtradas por estado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificaciones filtradas correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<NotificacionModel>> buscarPorEstado(
            @Parameter(description = "Estado de la notificación", example = "PENDIENTE")
            @PathVariable String estado) {

        return ResponseEntity.ok(notificacionService.buscarPorEstado(estado));
    }

    @Operation(
            summary = "Actualizar notificación",
            description = "Actualiza los datos de una notificación existente. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar notificaciones"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<NotificacionModel> actualizar(
            @Parameter(description = "ID de la notificación que se desea actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody NotificacionDTO dto) {

        return ResponseEntity.ok(notificacionService.actualizar(id, dto));
    }

    @Operation(
            summary = "Marcar notificación como enviada",
            description = "Actualiza el estado de una notificación para marcarla como enviada. Requiere rol ADMIN o RECEPCIONISTA."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación marcada como enviada"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para modificar notificaciones"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PatchMapping("/{id}/enviar")
    public ResponseEntity<NotificacionModel> marcarComoEnviada(
            @Parameter(description = "ID de la notificación que se marcará como enviada", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(notificacionService.marcarComoEnviada(id));
    }

    @Operation(
            summary = "Eliminar notificación",
            description = "Elimina una notificación del sistema según su ID. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Notificación eliminada correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar notificaciones"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la notificación que se desea eliminar", example = "1")
            @PathVariable Long id) {

        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}