package com.hoteltransilvania.clientes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoteltransilvania.clientes.dto.ClienteDTO;
import com.hoteltransilvania.clientes.dto.RespuestaDTO.ClienteRespuestaDTO;
import com.hoteltransilvania.clientes.exception.DuplicateResourceException;
import com.hoteltransilvania.clientes.exception.ResourceNotFoundException;
import com.hoteltransilvania.clientes.model.ClienteModel;
import com.hoteltransilvania.clientes.repository.ClienteRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteService (ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    public ClienteRespuestaDTO guardar(ClienteDTO dto){

        log.info("Iniciando registro de cliente con correo: {}", dto.getCorreo());

        //Aca se verifica si el correo ya se encuentra registrado
        if (clienteRepository.existsByCorreo(dto.getCorreo())) {

            log.warn(
                    "Intento de registro duplicado para correo: {}",
                    dto.getCorreo()); //Genera un log en consola indicando que el correo esta duplicado

            throw new DuplicateResourceException(
                    "El correo: "
                    + dto.getCorreo()
                    + ", ya se encuentra REGISTRADO");
        }

        ClienteModel cliente = new ClienteModel();

        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setCorreo(dto.getCorreo());
        cliente.setTelefono(dto.getTelefono());

        ClienteModel clienteGuardado = clienteRepository.save(cliente);

        log.info(
                "Cliente registrado correctamente ID: {}",
                clienteGuardado.getId());

        ClienteRespuestaDTO respuesta = new ClienteRespuestaDTO();

        respuesta.setNombre(
                clienteGuardado.getNombre()
                + " "
                + clienteGuardado.getApellido());

        respuesta.setMensaje(
                "Registro exitoso");

        return respuesta;
    }

    public List<ClienteModel> listarTodos(){

        log.info("Listando todos los clientes");

        return clienteRepository.findAll();
    }

    public ClienteModel buscarPorId(Long id) {

        log.info(
                "Buscando cliente ID: {}",
                id);

        return clienteRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Cliente no encontrado ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Cliente no encontrado con ID: "
                            + id);
                });
    }

    public ClienteModel actualizar(
            Long id,
            ClienteDTO dto) {

        log.info(
                "Actualizando cliente ID: {}",
                id);

        ClienteModel cliente =
                clienteRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Cliente no encontrado para actualización ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Cliente no encontrado con ID: "
                            + id);
                });

        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setCorreo(dto.getCorreo());
        cliente.setTelefono(dto.getTelefono());

        ClienteModel actualizado =
                clienteRepository.save(cliente);

        log.info(
                "Cliente actualizado correctamente ID: {}",
                id);

        return actualizado;
    }

    public void eliminar(Long id) {

        log.warn(
                "Eliminando cliente ID: {}",
                id);

        ClienteModel cliente =
                clienteRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Cliente no encontrado para eliminar ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Cliente no encontrado con ID: "
                            + id);
                });

        clienteRepository.delete(cliente);

        log.info(
                "Cliente eliminado correctamente ID: {}",
                id);
    }

}