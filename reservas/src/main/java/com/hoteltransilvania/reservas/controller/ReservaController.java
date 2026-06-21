package com.hoteltransilvania.reservas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hoteltransilvania.reservas.Model.ReservaModel;
import com.hoteltransilvania.reservas.dto.ReservaDTO;
import com.hoteltransilvania.reservas.dto.RespuestaDTO.ReservaRespuestaDTO;
import com.hoteltransilvania.reservas.service.ReservaServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Reservas", description = "Módulo de gestión de reservas del hotel")
@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaServices reservaServices;

    public ReservaController(ReservaServices reservaServices) {
        this.reservaServices = reservaServices;
    }

    @Operation(
            summary = "Crear reserva",
            description = "Registra una nueva reserva asociada a un cliente y una habitación. Requiere rol ADMIN o RECEPCIONISTA."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para crear reservas"),
            @ApiResponse(responseCode = "404", description = "Cliente o habitación no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto: habitación no disponible o reserva duplicada")
    })
    @PostMapping
    public ResponseEntity<ReservaRespuestaDTO> crear(@Valid @RequestBody ReservaDTO dto) {
        return new ResponseEntity<>(reservaServices.guardar(dto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar reservas",
            description = "Retorna todas las reservas registradas. Requiere rol ADMIN o RECEPCIONISTA."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservas listadas correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping
    public ResponseEntity<List<ReservaModel>> listar() {
        List<ReservaModel> lista = reservaServices.listarTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(
            summary = "Obtener reserva por ID",
            description = "Retorna una reserva específica según su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservaModel> buscarPorId(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Long id) {

        return new ResponseEntity<>(reservaServices.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Actualizar reserva",
            description = "Actualiza los datos de una reserva existente. Requiere rol ADMIN o RECEPCIONISTA."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar reservas"),
            @ApiResponse(responseCode = "404", description = "Reserva, cliente o habitación no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto: habitación no disponible")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReservaModel> modificar(
            @Parameter(description = "ID de la reserva que se desea actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ReservaDTO dto) {

        ReservaModel reservaActualizada = reservaServices.modificar(id, dto);
        return new ResponseEntity<>(reservaActualizada, HttpStatus.OK);
    }

    @Operation(
            summary = "Eliminar reserva",
            description = "Elimina una reserva del sistema según su ID. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reserva eliminada correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar reservas"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la reserva que se desea eliminar", example = "1")
            @PathVariable Long id) {

        reservaServices.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}