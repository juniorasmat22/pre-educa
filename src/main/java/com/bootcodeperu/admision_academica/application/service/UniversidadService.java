package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.universidad.UniversidadRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.universidad.UniversidadResponse;

import java.util.List;

public interface UniversidadService {
    UniversidadResponse crear(UniversidadRequest request);

    UniversidadResponse actualizar(Long id, UniversidadRequest request);

    UniversidadResponse obtenerPorId(Long id);

    List<UniversidadResponse> listarTodas();

    void eliminar(Long id);
}
