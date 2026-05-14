package com.hoteltransilvania.privilegios.repository;

import com.hoteltransilvania.privilegios.models.RolPrivilegio;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolPrivilegioRepository
        extends JpaRepository<RolPrivilegio, Long> {

    List<RolPrivilegio> findByRolId(Long rolId);
}