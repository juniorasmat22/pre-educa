package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.catalogo.CatalogoResponse;

import java.util.List;

public interface CatalogoService {
    List<CatalogoResponse> obtenerCatalogoCompleto();
}
