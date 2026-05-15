package com.hoteltransilvania.clientes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoteltransilvania.clientes.model.ClienteModel;
import com.hoteltransilvania.clientes.dto.ClienteDTO;
import com.hoteltransilvania.clientes.dto.RespuestaDTO.ClienteRespuestaDTO;
import com.hoteltransilvania.clientes.exception.DuplicateResourceException;
import com.hoteltransilvania.clientes.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired //Instancio a Cliente Repository
    private ClienteRepository clienteRepository;

    public ClienteRespuestaDTO guardar(ClienteDTO dto){

        if (clienteRepository.existsByCorreo(dto.getCorreo())) {
            throw new DuplicateResourceException("El correo: "+dto.getCorreo()+", ya se encuentra REGISTRADO"); 
        }
        ClienteModel cliente = new ClienteModel();
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setCorreo(dto.getCorreo());
        cliente.setTelefono(dto.getTelefono());

        ClienteModel clienteGuardado = clienteRepository.save(cliente);

        ClienteRespuestaDTO respuesta = new ClienteRespuestaDTO();
        respuesta.setNombre(clienteGuardado.getNombre() + " " + clienteGuardado.getApellido());
        respuesta.setMensaje("Registro exitoso");

        return respuesta;
    }

    public List<ClienteModel> listarTodos(){
        return clienteRepository.findAll();
    }

    public ClienteModel buscarPorId(Long id) {
    return clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }


}

