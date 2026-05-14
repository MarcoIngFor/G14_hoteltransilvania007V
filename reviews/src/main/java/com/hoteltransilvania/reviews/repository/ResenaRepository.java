package com.hoteltransilvania.reviews.repository;

import com.hoteltransilvania.reviews.entity.Resena;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResenaRepository
        extends JpaRepository<Resena, Long> {

    List<Resena> findByHabitacionId(Long habitacionId);
}