package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolPermisosRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolUpdateRequest;

import java.util.List;

public interface RolService {
    List<RolResponse> getAllRoles();
    RolResponse getRolById(Long id);
    RolResponse createRol(RolRequest request);
    RolResponse updateRol(Long id, RolUpdateRequest request);
    RolResponse addPermisos(Long id, RolPermisosRequest request);
    RolResponse removePermisos(Long id, RolPermisosRequest request);
}
