package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaResponse;

public interface TeoriaAdminService {
    ContenidoTeoriaResponse save(ContenidoTeoriaRequest request);

    ContenidoTeoriaResponse update(String id, ContenidoTeoriaRequest request);

    void delete(String id);
}
