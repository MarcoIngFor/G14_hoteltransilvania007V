package com.hoteltransilvania.clientesservice.service;

import com.hoteltransilvania.clientesservice.entity.Cliente;
import com.hoteltransilvania.clientesservice.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }

    public void eliminarCliente(Long id) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        clienteRepository.delete(existente);
    }

    public Cliente actualizarCliente(Long id, Cliente cliente) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        existente.setNombre(cliente.getNombre());
        existente.setApellido(cliente.getApellido());
        existente.setCorreo(cliente.getCorreo());
        existente.setTelefono(cliente.getTelefono());

        return clienteRepository.save(existente);
    }
}