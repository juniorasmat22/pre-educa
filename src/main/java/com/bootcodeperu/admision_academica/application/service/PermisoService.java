package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolResponseMini;

import java.util.List;
import java.util.Set;

public interface PermisoService {
    List<PermisoResponse> getAllPermisos();
    PermisoResponse createPermiso(PermisoRequest request);
    PermisoResponse updatePermiso(Long id, PermisoRequest request);

    void deletePermiso(Long id);

    PermisoResponse getPermisoById(Long id);

    Set<RolResponseMini> getRolesByPermiso(Long id);
}
