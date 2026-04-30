package com.hoteltransilvania.clientesservice.repository;

// Importamos la entidad Cliente
import com.hoteltransilvania.clientesservice.entity.Cliente;

// Importamos JpaRepository (trae todo listo)
import org.springframework.data.jpa.repository.JpaRepository;

// Esta interfaz conecta con la base de datos
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // No necesitas escribir nada más por ahora

    // Métodos que ya tienes disponibles:
    // save()        -> guardar cliente
    // findAll()     -> listar todos
    // findById()    -> buscar por ID
    // deleteById() -> eliminar
}