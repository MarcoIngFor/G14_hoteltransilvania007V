package com.hoteltransilvania.clientes.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.hoteltransilvania.clientes.dto.ClienteDTO;
import com.hoteltransilvania.clientes.dto.RespuestaDTO.ClienteRespuestaDTO;
import com.hoteltransilvania.clientes.exception.DuplicateResourceException;
import com.hoteltransilvania.clientes.exception.ResourceNotFoundException;
import com.hoteltransilvania.clientes.model.ClienteModel;
import com.hoteltransilvania.clientes.repository.ClienteRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    @DisplayName("Debe guardar cliente correctamente")
    void shouldSaveClienteSuccessfully() {

        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Jonathan");
        dto.setApellido("Harker");
        dto.setCorreo("jonathan.harker@email.com");
        dto.setTelefono("+56912345678");

        ClienteModel clienteGuardado = new ClienteModel();
        clienteGuardado.setId(1L);
        clienteGuardado.setNombre("Jonathan");
        clienteGuardado.setApellido("Harker");
        clienteGuardado.setCorreo("jonathan.harker@email.com");
        clienteGuardado.setTelefono("+56912345678");

        when(clienteRepository.existsByCorreo(dto.getCorreo()))
                .thenReturn(false);

        when(clienteRepository.save(any(ClienteModel.class)))
                .thenReturn(clienteGuardado);

        ClienteRespuestaDTO respuesta = clienteService.guardar(dto);

        assertNotNull(respuesta);
        assertEquals("Jonathan Harker", respuesta.getNombre());
        assertEquals("Registro exitoso", respuesta.getMensaje());

        verify(clienteRepository).existsByCorreo(dto.getCorreo());
        verify(clienteRepository).save(any(ClienteModel.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el correo ya existe")
    void shouldThrowExceptionWhenCorreoAlreadyExists() {

        ClienteDTO dto = new ClienteDTO();
        dto.setCorreo("jonathan.harker@email.com");

        when(clienteRepository.existsByCorreo(dto.getCorreo()))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> clienteService.guardar(dto)
        );

        verify(clienteRepository).existsByCorreo(dto.getCorreo());
        verify(clienteRepository, never()).save(any(ClienteModel.class));
    }

    @Test
    @DisplayName("Debe listar todos los clientes")
    void shouldListAllClientes() {

        ClienteModel cliente1 = new ClienteModel();
        cliente1.setId(1L);
        cliente1.setNombre("Jonathan");

        ClienteModel cliente2 = new ClienteModel();
        cliente2.setId(2L);
        cliente2.setNombre("Mina");

        when(clienteRepository.findAll())
                .thenReturn(List.of(cliente1, cliente2));

        List<ClienteModel> resultado = clienteService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Jonathan", resultado.get(0).getNombre());
        assertEquals("Mina", resultado.get(1).getNombre());

        verify(clienteRepository).findAll();
    }

    @Test
    @DisplayName("Debe retornar cliente cuando el ID existe")
    void shouldReturnClienteWhenIdExists() {

        ClienteModel cliente = new ClienteModel();
        cliente.setId(1L);
        cliente.setNombre("Jonathan");
        cliente.setApellido("Harker");
        cliente.setCorreo("jonathan.harker@email.com");

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        ClienteModel resultado = clienteService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Jonathan", resultado.getNombre());
        assertEquals("Harker", resultado.getApellido());
        assertEquals("jonathan.harker@email.com", resultado.getCorreo());

        verify(clienteRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el cliente no existe")
    void shouldThrowExceptionWhenClienteDoesNotExist() {

        when(clienteRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> clienteService.buscarPorId(99L)
        );

        verify(clienteRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe actualizar cliente cuando el ID existe")
    void shouldUpdateClienteWhenIdExists() {

        ClienteModel clienteExistente = new ClienteModel();
        clienteExistente.setId(1L);
        clienteExistente.setNombre("Jonathan");
        clienteExistente.setApellido("Harker");
        clienteExistente.setCorreo("jonathan.harker@email.com");
        clienteExistente.setTelefono("+56911111111");

        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Mina");
        dto.setApellido("Murray");
        dto.setCorreo("mina.murray@email.com");
        dto.setTelefono("+56922222222");

        ClienteModel clienteActualizado = new ClienteModel();
        clienteActualizado.setId(1L);
        clienteActualizado.setNombre("Mina");
        clienteActualizado.setApellido("Murray");
        clienteActualizado.setCorreo("mina.murray@email.com");
        clienteActualizado.setTelefono("+56922222222");

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(clienteExistente));

        when(clienteRepository.save(clienteExistente))
                .thenReturn(clienteActualizado);

        ClienteModel resultado = clienteService.actualizar(1L, dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Mina", resultado.getNombre());
        assertEquals("Murray", resultado.getApellido());
        assertEquals("mina.murray@email.com", resultado.getCorreo());
        assertEquals("+56922222222", resultado.getTelefono());

        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(clienteExistente);
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar cliente inexistente")
    void shouldThrowExceptionWhenUpdatingClienteDoesNotExist() {

        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Mina");

        when(clienteRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> clienteService.actualizar(99L, dto)
        );

        verify(clienteRepository).findById(99L);
        verify(clienteRepository, never()).save(any(ClienteModel.class));
    }

    @Test
    @DisplayName("Debe eliminar cliente cuando el ID existe")
    void shouldDeleteClienteWhenIdExists() {

        ClienteModel cliente = new ClienteModel();
        cliente.setId(1L);
        cliente.setNombre("Jonathan");

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        clienteService.eliminar(1L);

        verify(clienteRepository).findById(1L);
        verify(clienteRepository).delete(cliente);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar cliente inexistente")
    void shouldThrowExceptionWhenDeletingClienteDoesNotExist() {

        when(clienteRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> clienteService.eliminar(99L)
        );

        verify(clienteRepository).findById(99L);
        verify(clienteRepository, never()).delete(any(ClienteModel.class));
    }
}