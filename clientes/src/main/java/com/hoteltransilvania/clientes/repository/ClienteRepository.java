package com.hoteltransilvania.clientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hoteltransilvania.clientes.model.ClienteModel;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Long>{
    boolean existsByCorreo(String correo);
}
