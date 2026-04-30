package com.hoteltransilvania.clientesservice.service;

// Importamos la entidad Cliente
import com.hoteltransilvania.clientesservice.entity.Cliente;

// Importamos el repositorio para hablar con la base de datos
import com.hoteltransilvania.clientesservice.repository.ClienteRepository;

// Anotación para indicar que esta clase es un servicio
import org.springframework.stereotype.Service;

// Importamos List para devolver listas de clientes
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    // Aquí guardamos el repositorio que se conecta a la base de datos

    public ClienteService(ClienteRepository clienteRepository) {
        // Spring entrega automáticamente el repository mediante el constructor
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        // Busca todos los clientes en la base de datos
        return clienteRepository.findAll();
    }

    public Cliente guardarCliente(Cliente cliente) {
        // Guarda un cliente en la base de datos
        return clienteRepository.save(cliente);
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public Cliente actualizarCliente(Long id, Cliente cliente) {
        Cliente existente = clienteRepository.findById(id).orElse(null);

        if (existente == null) {
            return null;
        }

        existente.setNombre(cliente.getNombre());
        existente.setApellido(cliente.getApellido());
        existente.setCorreo(cliente.getCorreo());
        existente.setTelefono(cliente.getTelefono());

        return clienteRepository.save(existente);
    }
}