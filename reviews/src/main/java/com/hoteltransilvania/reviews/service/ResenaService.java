package com.hoteltransilvania.reviews.service;

import com.hoteltransilvania.reviews.entity.Resena;
import com.hoteltransilvania.reviews.repository.ResenaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ResenaService {

    private final ResenaRepository resenaRepository;

    public ResenaService(
            ResenaRepository resenaRepository
    ) {
        this.resenaRepository = resenaRepository;
    }

    // LISTAR
    public List<Resena> listar() {
        return resenaRepository.findAll();
    }

    // OBTENER POR ID
    public Resena obtenerPorId(Long id) {

        return resenaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Reseña no encontrada"
                        )
                );
    }

    // GUARDAR
    public Resena guardar(Resena resena) {

        resena.setFecha(LocalDate.now());

        return resenaRepository.save(resena);
    }

    // ELIMINAR
    public void eliminar(Long id) {

        Resena resena = obtenerPorId(id);

        resenaRepository.delete(resena);
    }

    // BUSCAR POR HABITACION
    public List<Resena> buscarPorHabitacion(
            Long habitacionId
    ) {

        return resenaRepository.findByHabitacionId(
                habitacionId
        );
    }
}