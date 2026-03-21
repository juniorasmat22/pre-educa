package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.procesoadmision.ProcesoAdmisionRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.procesoadmision.ProcesoAdmisionResponse;

import java.util.List;

public interface ProcesoAdmisionService {
    ProcesoAdmisionResponse crear(ProcesoAdmisionRequest request);

    ProcesoAdmisionResponse actualizar(Long id, ProcesoAdmisionRequest request);

    ProcesoAdmisionResponse obtenerPorId(Long id);

    List<ProcesoAdmisionResponse> listarTodos();

    void eliminar(Long id);
}