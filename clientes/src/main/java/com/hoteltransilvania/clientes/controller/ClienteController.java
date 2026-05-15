package com.hoteltransilvania.clientes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoteltransilvania.clientes.model.ClienteModel;
import com.hoteltransilvania.clientes.dto.ClienteDTO;
import com.hoteltransilvania.clientes.dto.RespuestaDTO.ClienteRespuestaDTO;
import com.hoteltransilvania.clientes.service.ClienteService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/clientes")

public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteRespuestaDTO> crear (@Valid @RequestBody ClienteDTO dto){
        return new ResponseEntity<>(clienteService.guardar(dto),HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClienteModel>> listar() {
        List<ClienteModel> clientes = clienteService.listarTodos();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteModel> buscarPorId(@PathVariable Long id) {
        ClienteModel cliente = clienteService.buscarPorId(id);
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }
    

}