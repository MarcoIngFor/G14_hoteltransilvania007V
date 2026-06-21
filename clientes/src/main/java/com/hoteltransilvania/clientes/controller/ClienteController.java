package com.hoteltransilvania.clientes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hoteltransilvania.clientes.model.ClienteModel;
import com.hoteltransilvania.clientes.dto.ClienteDTO;
import com.hoteltransilvania.clientes.dto.RespuestaDTO.ClienteRespuestaDTO;
import com.hoteltransilvania.clientes.dto.RespuestaDTO.EliminarRespuestaDTO;
import com.hoteltransilvania.clientes.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Clientes", description = "Módulo de gestión de clientes del hotel")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(
            summary = "Crear cliente",
            description = "Registra un nuevo cliente en el sistema. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para crear clientes"),
            @ApiResponse(responseCode = "409", description = "Conflicto: correo de cliente ya registrado")
    })
    @PostMapping
    public ResponseEntity<ClienteRespuestaDTO> crear(@Valid @RequestBody ClienteDTO dto) {
        return new ResponseEntity<>(clienteService.guardar(dto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar clientes",
            description = "Retorna todos los clientes registrados. Requiere rol ADMIN o RECEPCIONISTA."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clientes listados correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder")
    })
    @GetMapping
    public ResponseEntity<List<ClienteModel>> listar() {
        List<ClienteModel> clientes = clienteService.listarTodos();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @Operation(
            summary = "Obtener cliente por ID",
            description = "Retorna la información de un cliente según su ID. Requiere rol ADMIN o RECEPCIONISTA."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder al recurso"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteModel> buscarPorId(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long id) {

        ClienteModel cliente = clienteService.buscarPorId(id);
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @Operation(
            summary = "Actualizar cliente",
            description = "Actualiza la información de un cliente existente según su ID. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar clientes"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto: correo de cliente ya registrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteRespuestaDTO> actualizar(
            @Parameter(description = "ID del cliente que se desea actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ClienteDTO dto) {

        ClienteModel clienteActualizado = clienteService.actualizar(id, dto);

        ClienteRespuestaDTO respuesta = new ClienteRespuestaDTO();
        respuesta.setNombre(clienteActualizado.getNombre());
        respuesta.setMensaje("El cliente: "
                + clienteActualizado.getNombre()
                + ", ha sido modificado con éxito");

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Operation(
            summary = "Eliminar cliente",
            description = "Elimina un cliente del sistema según su ID. Requiere rol ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o inválido"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar clientes"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<EliminarRespuestaDTO> eliminar(
            @Parameter(description = "ID del cliente que se desea eliminar", example = "1")
            @PathVariable Long id) {

        clienteService.eliminar(id);

        return ResponseEntity.ok(
                new EliminarRespuestaDTO(
                        "Cliente con id: " + id + " ha sido eliminado"));
    }
}