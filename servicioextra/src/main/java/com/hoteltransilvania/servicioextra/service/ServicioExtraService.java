package com.hoteltransilvania.servicioextra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hoteltransilvania.servicioextra.Model.ServicioExtraModel;
import com.hoteltransilvania.servicioextra.Model.TipoServicioModel;
import com.hoteltransilvania.servicioextra.dto.ServicioExtraDTO;
import com.hoteltransilvania.servicioextra.dto.RespuestaDTO.SerExtraDTO;
import com.hoteltransilvania.servicioextra.exception.ResourceNotFoundException;
import com.hoteltransilvania.servicioextra.repository.ServicioExtraRepository;
import com.hoteltransilvania.servicioextra.repository.TipoServicioRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ServicioExtraService {

    private ServicioExtraRepository servicioExtraRepository;
    private TipoServicioRepository tipoServicioRepository;

    ServicioExtraService(
            ServicioExtraRepository servicioExtraRepository,
            TipoServicioRepository tipoServicioRepository){

        this.servicioExtraRepository = servicioExtraRepository;
        this.tipoServicioRepository = tipoServicioRepository;
    }

    // LISTAR
    public List<ServicioExtraModel> listarTodos(){

        log.info("Listando todos los servicios extra");

        return servicioExtraRepository.findAll();
    }

    // BUSCAR POR ID
    public ServicioExtraModel obtenerPorId(Long id){

        log.info(
                "Buscando servicio extra ID: {}",
                id);

        return servicioExtraRepository.findById(id)
                .orElseThrow(() -> {

                    log.error(
                            "Servicio extra no encontrado ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Servicio extra no encontrado con id: "
                            + id);
                });
    }

    // GUARDAR
    public SerExtraDTO guardar(ServicioExtraDTO dto){

        log.info(
                "Registrando servicio extra: {}",
                dto.getNombre());

        TipoServicioModel tipo =
                tipoServicioRepository
                .findById(dto.getIdTipoServicio())
                .orElseThrow(() -> {

                    log.error(
                            "Tipo servicio no encontrado ID: {}",
                            dto.getIdTipoServicio());

                    return new ResourceNotFoundException(
                            "El tipo de servicio con ID "
                            + dto.getIdTipoServicio()
                            + " no existe.");
                });

        ServicioExtraModel model =
                new ServicioExtraModel();

        model.setNombre(dto.getNombre());
        model.setDescripcion(dto.getDescripcion());
        model.setPrecio(dto.getPrecio());

        model.setTipoServicio(tipo);

        ServicioExtraModel servextra =
                servicioExtraRepository.save(model);

        log.info(
                "Servicio extra registrado correctamente ID: {}",
                servextra.getId());

        SerExtraDTO respuesta =
                new SerExtraDTO();

        respuesta.setMessage(
                "Servicio: "
                + servextra.getNombre()
                + ", agregado Exitosamente");

        return respuesta;
    }

    // ACTUALIZAR
    public ServicioExtraModel actualizar(
            Long id,
            ServicioExtraDTO dto) {

        log.info(
                "Actualizando servicio extra ID: {}",
                id);

        ServicioExtraModel servicio =
                servicioExtraRepository.findById(id)
                .orElseThrow(() -> {

                    log.error(
                            "Servicio extra no encontrado para actualizar ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Servicio extra no encontrado con ID: "
                            + id);
                });

        TipoServicioModel tipo =
                tipoServicioRepository
                .findById(dto.getIdTipoServicio())
                .orElseThrow(() -> {

                    log.error(
                            "Tipo servicio no encontrado al actualizar ID: {}",
                            dto.getIdTipoServicio());

                    return new ResourceNotFoundException(
                            "Tipo de servicio no encontrado con ID: "
                            + dto.getIdTipoServicio());
                });

        servicio.setNombre(dto.getNombre());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setPrecio(dto.getPrecio());
        servicio.setTipoServicio(tipo);

        ServicioExtraModel actualizado =
                servicioExtraRepository.save(servicio);

        log.info(
                "Servicio extra actualizado correctamente ID: {}",
                id);

        return actualizado;
    }

    // ELIMINAR
    public void eliminar(Long id) {

        log.warn(
                "Eliminando servicio extra ID: {}",
                id);

        ServicioExtraModel servicio =
                servicioExtraRepository.findById(id)
                .orElseThrow(() -> {

                    log.error(
                            "Servicio extra no encontrado para eliminar ID: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Servicio extra no encontrado con ID: "
                            + id);
                });

        servicioExtraRepository.delete(servicio);

        log.info(
                "Servicio extra eliminado correctamente ID: {}",
                id);
    }
}