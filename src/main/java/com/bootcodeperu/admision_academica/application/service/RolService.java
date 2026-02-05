package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolResponse;

import java.util.List;

public interface RolService {
    List<RolResponse> getAllRoles();
    RolResponse createRol(RolRequest request);
}
