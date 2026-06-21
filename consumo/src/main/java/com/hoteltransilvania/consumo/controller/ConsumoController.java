package com.hoteltransilvania.consumo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hoteltransilvania.consumo.dto.ConsumoDTO;
import com.hoteltransilvania.consumo.dto.RespuestaDTO.ConsumoRespuestaDTO;
import com.hoteltransilvania.consumo.model.ConsumoModel;
import com.hoteltransilvania.consumo.service.ConsumoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Consumos", description = "Módulo de gestión de consumos realizados por clientes")
@RestController
@RequestMapping("/consumo")
public class ConsumoController {

    private final ConsumoService consumoService;

    public ConsumoController(ConsumoService consumoService) {
        this.consumoService = consumoService;
    }

    @Operation(
            summary = "Registrar consumo",
            description = "Registra un consumo asociado a una reserva, cliente, habitación y servicio extra. Requiere rol ADMIN o RECEPCIONISTA."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consumo registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para registrar consumos"),
            @ApiResponse(responseCode = "404", description = "Recurso asociado no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos")
    })
    @PostMapping("/registrar")
    public ResponseEntity<ConsumoRespuestaDTO> registrar(@Valid @RequestBody ConsumoDTO dto) {
        return new ResponseEntity<>(consumoService.guardar(dto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar consumos",
            description = "Retorna todos los consumos registrados. Requiere rol ADMIN, RECEPCIONISTA o CAJERO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consumos listados correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<ConsumoModel>> listar() {
        return new ResponseEntity<>(consumoService.listarTodos(), HttpStatus.OK);
    }

    @Operation(
            summary = "Obtener consumo por ID",
            description = "Retorna un consumo específico según su ID. Requiere rol ADMIN, RECEPCIONISTA o CAJERO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consumo encontrado"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder"),
            @ApiResponse(responseCode = "404", description = "Consumo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConsumoModel> buscarPorId(
            @Parameter(description = "ID del consumo", example = "1")
            @PathVariable Long id) {

        return new ResponseEntity<>(consumoService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Buscar consumos por cliente",
            description = "Retorna los consumos asociados a un cliente específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consumos del cliente listados correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<ConsumoModel>> buscarPorCliente(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long idCliente) {

        return new ResponseEntity<>(consumoService.buscarPorCliente(idCliente), HttpStatus.OK);
    }

    @Operation(
            summary = "Buscar consumos por reserva",
            description = "Retorna los consumos asociados a una reserva específica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consumos de la reserva listados correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<ConsumoModel>> buscarPorReserva(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Long idReserva) {

        return new ResponseEntity<>(consumoService.buscarPorReserva(idReserva), HttpStatus.OK);
    }

    @Operation(
            summary = "Buscar consumos por habitación",
            description = "Retorna los consumos asociados a una habitación específica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consumos de la habitación listados correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping("/habitacion/{idHabitacion}")
    public ResponseEntity<List<ConsumoModel>> buscarPorHabitacion(
            @Parameter(description = "ID de la habitación", example = "1")
            @PathVariable Long idHabitacion) {

        return new ResponseEntity<>(consumoService.buscarPorHabitacion(idHabitacion), HttpStatus.OK);
    }

    @Operation(
            summary = "Actualizar consumo",
            description = "Actualiza un consumo existente según su ID. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consumo actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar consumos"),
            @ApiResponse(responseCode = "404", description = "Consumo no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ConsumoModel> actualizar(
            @Parameter(description = "ID del consumo que se desea actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ConsumoDTO dto) {

        ConsumoModel actualizado = consumoService.actualizar(id, dto);
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    @Operation(
            summary = "Eliminar consumo",
            description = "Elimina un consumo del sistema según su ID. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Consumo eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar consumos"),
            @ApiResponse(responseCode = "404", description = "Consumo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del consumo que se desea eliminar", example = "1")
            @PathVariable Long id) {

        consumoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}