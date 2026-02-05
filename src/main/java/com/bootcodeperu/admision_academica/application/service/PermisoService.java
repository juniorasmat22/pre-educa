package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoResponse;

import java.util.List;

public interface PermisoService {
    List<PermisoResponse> getAllPermisos();
    PermisoResponse createPermiso(PermisoRequest request);
}
