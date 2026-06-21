package com.hoteltransilvania.servicioextra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hoteltransilvania.servicioextra.Model.ServicioExtraModel;
import com.hoteltransilvania.servicioextra.Model.TipoServicioModel;
import com.hoteltransilvania.servicioextra.dto.TipoServicioDTO;
import com.hoteltransilvania.servicioextra.dto.RespuestaDTO.SerExtraDTO;
import com.hoteltransilvania.servicioextra.exception.ResourceNotFoundException;
import com.hoteltransilvania.servicioextra.repository.TipoServicioRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TipoServicioService {

    private TipoServicioRepository tipoServicioRepository;

    TipoServicioService(TipoServicioRepository tipoServicioRepository){
        this.tipoServicioRepository = tipoServicioRepository;
    }

    public List<TipoServicioModel> listarTodos(){

        log.info("Listando todos los tipos de servicio");

        return tipoServicioRepository.findAll();
    }

    public SerExtraDTO guardar(TipoServicioDTO dto){

        log.info(
                "Registrando tipo de servicio: {}",
                dto.getNombre());

        TipoServicioModel model =
                new TipoServicioModel();

        model.setNombre(dto.getNombre());

        TipoServicioModel tiposerv =
                tipoServicioRepository.save(model);

        log.info(
                "Tipo de servicio registrado correctamente ID: {}",
                tiposerv.getId());

        SerExtraDTO respuesta =
                new SerExtraDTO();

        respuesta.setMessage(
                "Tipo Servicio: "
                + tiposerv.getNombre()
                + ", agregado Exitosamente");

        return respuesta;
    }

    public void eliminar(Long id) {

        log.warn(
                "Eliminando servicio ID: {}",
                id);

        TipoServicioModel servicio =
                tipoServicioRepository.findById(id)
                .orElseThrow(() -> {log.error("Servicio no encontrado para eliminar ID: {}",id);

                    return new ResourceNotFoundException("Servicio no encontrado con ID: "+ id);
                });

        tipoServicioRepository.delete(servicio);

        log.info(
                "Servicio eliminado correctamente ID: {}",
                id);
    }
}