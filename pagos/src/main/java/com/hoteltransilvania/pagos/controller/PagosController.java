package com.hoteltransilvania.pagos.controller;

import com.hoteltransilvania.pagos.dto.PagosDTO;
import com.hoteltransilvania.pagos.model.PagosModel;
import com.hoteltransilvania.pagos.service.PagosService;

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

@Tag(name = "Pagos", description = "Módulo de gestión de pagos de reservas")
@RestController
@RequestMapping("/pagos")
public class PagosController {

    private final PagosService pagosService;

    public PagosController(PagosService pagosService) {
        this.pagosService = pagosService;
    }

    @Operation(
            summary = "Listar pagos",
            description = "Retorna todos los pagos registrados. Requiere rol ADMIN o CAJERO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagos listados correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping
    public ResponseEntity<List<PagosModel>> listar() {
        return new ResponseEntity<>(pagosService.listar(), HttpStatus.OK);
    }

    @Operation(
            summary = "Obtener pago por ID",
            description = "Retorna un pago específico según su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PagosModel> buscarPorId(
            @Parameter(description = "ID del pago", example = "1")
            @PathVariable Long id) {

        return new ResponseEntity<>(pagosService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Buscar pago por reserva",
            description = "Retorna el pago asociado a una reserva específica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago de la reserva encontrado"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado para la reserva")
    })
    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<PagosModel> buscarPorReserva(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Long idReserva) {

        return new ResponseEntity<>(pagosService.buscarPorReserva(idReserva), HttpStatus.OK);
    }

    @Operation(
            summary = "Registrar pago",
            description = "Registra un nuevo pago asociado a una reserva. Requiere rol ADMIN o CAJERO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pago registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para registrar pagos"),
            @ApiResponse(responseCode = "404", description = "Reserva asociada no encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflicto: pago ya registrado")
    })
    @PostMapping
    public ResponseEntity<PagosModel> registrar(@Valid @RequestBody PagosDTO dto) {
        return new ResponseEntity<>(pagosService.registrar(dto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Actualizar pago",
            description = "Actualiza un pago existente según su ID. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar pagos"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PagosModel> actualizar(
            @Parameter(description = "ID del pago que se desea actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody PagosDTO dto) {

        return new ResponseEntity<>(pagosService.actualizar(id, dto), HttpStatus.OK);
    }

    @Operation(
            summary = "Eliminar pago",
            description = "Elimina un pago del sistema según su ID. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar pagos"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del pago que se desea eliminar", example = "1")
            @PathVariable Long id) {

        pagosService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}