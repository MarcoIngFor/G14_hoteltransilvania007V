package com.hoteltransilvania.clientesservice.controller;

// Importamos la entidad
import com.hoteltransilvania.clientesservice.entity.Cliente;

// Importamos el service (la lógica)
import com.hoteltransilvania.clientesservice.service.ClienteService;

import jakarta.validation.Valid;

// Importaciones para REST
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// Indica que esta clase será un controlador REST (API)
@RequestMapping("/clientes")
// Ruta base: http://localhost:8081/clientes
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        // Spring inyecta automáticamente el service
        this.clienteService = clienteService;
    }

    @GetMapping
    // Método GET → listar clientes
    public List<Cliente> listar() {
        return clienteService.listarClientes();
    }

    @PostMapping
    // Método POST → guardar cliente
    public Cliente guardar(@Valid @RequestBody Cliente cliente) {
        return clienteService.guardarCliente(cliente);
    }

    @GetMapping("/{id}")
    public Cliente obtenerPorId(@PathVariable Long id) {
        return clienteService.obtenerPorId(id);
    }

    @DeleteMapping("/{id}")
        public void eliminar(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
    }

    @PutMapping("/{id}")
        public Cliente actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteService.actualizarCliente(id, cliente);
    }
}